package myproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PulumiLocalAdapter {

    public static String DEFAULT_AWS_REGION = "us-east-1";

    static BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<>();
    static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 5, 5, TimeUnit.SECONDS, QUEUE);

    /**
     * Runs Pulumi Up
     */
    public static void up(LocalStackContainer container, String stack, File workDir) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("pulumi", "up", "--skip-preview");

        builder.directory(workDir);

        final PulumiConfig project = PulumiConfig.read(new File(workDir, "../Pulumi.yaml"));
        PulumiConfig.writeTestConfig(container.getEndpoint().toString(), new File(workDir, "../Pulumi." + stack + ".yaml"));

        Process process = builder.start();

        Future<?> stdout = EXECUTOR_SERVICE.submit( new StreamGobbler(process.getInputStream(), System.out::println));
        Future<?> stderr = EXECUTOR_SERVICE.submit( new StreamGobbler(process.getErrorStream(), System.out::println));

        int exitCode = process.waitFor();

        assertDoesNotThrow(() -> stdout.get(10, TimeUnit.SECONDS));
        assertDoesNotThrow(() -> stderr.get(10, TimeUnit.SECONDS));
        assertEquals(0, exitCode);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PulumiConfig {

        public String name;
        public String runtime;
        public String description;

        private static final String CONFIG_TEMPLATE_FILE = "Pulumi.LocalStack.yaml.template";

        private static PulumiConfig read(File pulumiYaml) throws IOException {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(pulumiYaml, PulumiConfig.class);
        }

        private static void writeTestConfig(String localStackEndpoint, File destination) throws IOException {
            final String template = loadConfigTemplate();
            HashMap<String,String> replacements = new HashMap<>();
            replacements.put("LOCALSTACK_ENDPOINT", localStackEndpoint);
            replacements.put("AWS_REGION", DEFAULT_AWS_REGION);
            final String pulumiConfig = update(template, replacements);

            try (FileWriter writer = new FileWriter(destination)) {
                BufferedWriter out = new BufferedWriter(writer);
                out.write(pulumiConfig);
                out.close();
            }
        }

        private static String loadConfigTemplate() throws IOException {
            try (InputStream is = PulumiLocalAdapter.class.getResourceAsStream("PulumiLocalAdapter/" + CONFIG_TEMPLATE_FILE)) {
                if (is == null) {
                    throw new IOException("Cannot load the template config resource from " + PulumiLocalAdapter.class + ":" + CONFIG_TEMPLATE_FILE);
                }
                try (InputStreamReader isr = new InputStreamReader(is);
                     BufferedReader reader = new BufferedReader(isr)) {
                    return reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            }
        }

        private static String update(String text, HashMap<String,String> replacements) {
            Pattern pattern = Pattern.compile("\\{(.+?)}");
            Matcher matcher = pattern.matcher(text);

            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (matcher.find()) {
                String replacement = replacements.get(matcher.group(1));
                builder.append(text.substring(i, matcher.start()));
                if (replacement == null)
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
            builder.append(text.substring(i, text.length()));
            return builder.toString();
        }
    }

    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

}
