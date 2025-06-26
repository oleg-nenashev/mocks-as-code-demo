package myproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Assert;
import org.testcontainers.containers.localstack.LocalStackContainer;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PulumiLocalStackAdapter {

    static BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<>();
    static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 5, 5, TimeUnit.SECONDS, QUEUE);

    static boolean CONFIGURED = false;
    static String STACK_NAME;

    private static final Logger LOGGER = Logger.getLogger(PulumiLocalStackAdapter.class.getName());

    public static void configure(LocalStackContainer container, String stack, File workDir) throws IOException {
        final PulumiConfig project = PulumiConfig.read(new File(workDir, "../Pulumi.yaml"));
        PulumiConfig.writeTestConfig(container, new File(workDir, "../Pulumi." + stack + ".yaml"));
        CONFIGURED = true;
        STACK_NAME = stack;
    }

    /**
     * Runs Pulumi Up
     */
    public static void up(File workDir) throws IOException, InterruptedException {
        run(workDir,"pulumi", "up", "--skip-preview");
    }

    public static void init(File workDir) throws IOException, InterruptedException {
        run(workDir,"pulumi", "stack", "init", STACK_NAME);
    }

    public static void clean(File workDir) throws IOException, InterruptedException {
        run(workDir,"pulumi", "stack", "rm", STACK_NAME);
    }

    public static void cancel(File workDir) throws IOException, InterruptedException {
        run(workDir,"pulumi", "up", "--skip-preview")  ;
    }

    public static void run(File workDir, String ... cli) throws IOException, InterruptedException {
        if (!CONFIGURED) {
            String message = "Pulumi adapter for LocalStack is not configured with " + PulumiLocalStackAdapter.class + "#configure()";
            LOGGER.severe(message);
            Assert.fail(message);
        }

        LOGGER.warning("Running " + Arrays.toString(cli));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cli);
        builder.directory(workDir);

        Process process = builder.start();

        Future<?> stdout = EXECUTOR_SERVICE.submit( new StreamGobbler(process.getInputStream(), System.out::println));
        Future<?> stderr = EXECUTOR_SERVICE.submit( new StreamGobbler(process.getErrorStream(), System.out::println));

        int exitCode = process.waitFor();

        assertDoesNotThrow(() -> stdout.get(10, TimeUnit.SECONDS));
        assertDoesNotThrow(() -> stderr.get(10, TimeUnit.SECONDS));
        assertEquals("Exit code is not null for: " + Arrays.toString(cli), 0, exitCode);
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

        private static void writeTestConfig(LocalStackContainer container, File destination) throws IOException {
            final String template = loadConfigTemplate();
            HashMap<String,String> replacements = new HashMap<>();
            replacements.put("LOCALSTACK_ENDPOINT", container.getEndpoint().toString());
            replacements.put("AWS_REGION", container.getRegion());
            replacements.put("AWS_ACCESS_KEY", container.getAccessKey());
            replacements.put("AWS_SECRET_KEY", container.getSecretKey());

            final String pulumiConfig = update(template, replacements);

            try (FileWriter writer = new FileWriter(destination)) {
                BufferedWriter out = new BufferedWriter(writer);
                out.write(pulumiConfig);
                out.close();
            }
        }

        private static String loadConfigTemplate() throws IOException {
            try (InputStream is = PulumiLocalStackAdapter.class.getResourceAsStream("PulumiLocalStackAdapter/" + CONFIG_TEMPLATE_FILE)) {
                if (is == null) {
                    throw new IOException("Cannot load the template config resource from " + PulumiLocalStackAdapter.class + ":" + CONFIG_TEMPLATE_FILE);
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
