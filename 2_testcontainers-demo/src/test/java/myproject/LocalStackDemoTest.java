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

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
public class LocalStackDemoTest {

    DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.4");

    @Container
    public LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(S3);

    @Test
    public void testClient() {
        S3Client s3 = S3Client
            .builder()
            .endpointOverride(localstack.getEndpoint())
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                    )
            )
            .region(Region.of(localstack.getRegion()))
            .build();
    }

}
