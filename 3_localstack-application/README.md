# Test Application

This test application is based on the demo repository from the
[Testing AWS service integrations using LocalStack](https://testcontainers.com/guides/testing-aws-service-integrations-using-localstack/)
guide.
We use the same technology stack in this demo, and within the
[Pulumi deployment](../3_deployment/).

## Testing AWS Service Integrations using LocalStack

This is sample code for [Testing AWS Service Integrations using LocalStack](https://testcontainers.com/guides/testing-aws-service-integrations-using-localstack) guide.

### 1. Setup Environment

Make sure you have Java 8+ and a [compatible Docker environment](https://www.testcontainers.org/supported_docker_environment/) installed.
The necessary Java version will be installed by Gradle.

### 2. Run the app

```shell
../gradlew bootUp
```

### 2. Run Tests

Run the command to run the tests.

```shell
$ ../gradlew test
```

The tests should pass.
