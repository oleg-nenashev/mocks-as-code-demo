package myproject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * A tests that uses the LocalStack Test image and pulumi local to deploy
 */
@Testcontainers
public class PulumiLocalTest {

    private static final String STACK_NAME = "mocks-as-code-demo";

    static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.4");

    @Container
    static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(Service.S3, Service.SQS);
    static File WORK_DIR;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        String QUEUE_NAME = UUID.randomUUID().toString();
        WORK_DIR = new File(".");
        PulumiLocalStackAdapter.configure(localstack, STACK_NAME, WORK_DIR);


        // Clean or Init the stack. We use it in parallel, and there might be stale resources Pulumi server does not know how delete from terminated LocalStack
        // TODO: check status automatically
        // PulumiLocalAdapter.init(WORK_DIR);
        // PulumiLocalAdapter.clean(WORK_DIR);

        localstack.execInContainer(
        "awslocal",
        "sqs",
        "create-queue",
        "--queue-name",
        QUEUE_NAME
        );

        System.setProperty("app.bucket", App.BUCKET_NAME);
        System.setProperty("app.queue", QUEUE_NAME);
        System.setProperty(
            "spring.cloud.aws.region.static",
            localstack.getRegion()
        );
        System.setProperty(
            "spring.cloud.aws.credentials.access-key",
            localstack.getAccessKey()
        );
        System.setProperty(
            "spring.cloud.aws.credentials.secret-key",
            localstack.getSecretKey()
        );
        System.setProperty(
            "spring.cloud.aws.endpoint",
            localstack.getEndpoint().toString()
        );
    }

    @Test
    public void testClient() throws Exception {

        // TODO: Move Init to BeforeClass

        PulumiLocalStackAdapter.up(WORK_DIR);
    }

}

