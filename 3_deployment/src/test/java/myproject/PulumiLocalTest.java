package myproject;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import java.io.File;

/**
 * A tests that uses the LocalStack Test image and pulumi local to deploy
 */
@Testcontainers
public class PulumiLocalTest {

    private static final String STACK_NAME = "mocks-as-code-demo";

    DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.4");

    @Container
    public LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(Service.S3, Service.EC2);

    @Test
    public void testClient() throws Exception {
        PulumiLocalAdapter.up(localstack, STACK_NAME, new File("."));
    }

}

