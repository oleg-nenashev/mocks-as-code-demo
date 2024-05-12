# Mocks as Code for Configuration Management. Demo

In this demo, we build a full-Java implementation for several key aspects of an application:

- The application itself
- Test Automation code - with Testcontainers, WireMock, LocalStack and other Java tools
- Configuration Management - with [Pulumi for Java](https://www.pulumi.com/docs/languages-sdks/java/)
- All the build logic - with [Gradle Build Tool](https://gradle.org/), a.k.a Gradle.
  We will also use [Gradle Build Scan](https://docs.gradle.org/current/userguide/build_scans.html) for troubleshooting
  the Pulumi SDK issues.

This demo is created for the Mocks as Code workshop by Oleg Nenashev.
The same demo setup can be implemented purely in Kotlin,
Golang, Python or any other language supported by Pulumi and Testcontainers.

## Prerequisites

- Installed Pulumi CLI application
- Testcontainers-compatible Docker Engine, preferably Docker Desktop
  (see the requirements [here](https://www.testcontainers.org/supported_docker_environment/))
- Java 17
- Any IDE that you like, tested on IntellijIDEA and VS Code

For Java tools, consider using [sdkman](https://sdkman.io/) for the demo to make it easier:

```shell
sdk env install
sdk env
```

## Workshop Steps

1. Setting up the environment
2. Testing AWS Service Integrations using LocalStack and Testcontainers,
   similar to [this guide](https://testcontainers.com/guides/testing-aws-service-integrations-using-localstack/)
3. Mocking service APIs with WireMock
4. Infrastructure-as-Code with Pulumi and Pulumi SDK for Java, and using the same stack for
   Pulumi deployment integration testing
5. Advanced troubleshooting and observability of the configurations with Gradle Build Scan

## Credits

The code implementation is based on the
[aws-java-webserver](https://github.com/pulumi/examples/blob/master/aws-java-webserver)
example from [pulumi/examples](https://github.com/pulumi/examples/).

Some bits of the demo are inspired by [Pulumi CLI fo LocalStack](https://github.com/localstack/pulumi-local) wrapper,
which is adapted inside the integration test.
The bits of the demo will be converted to a Java implementation for it,
contributions are welcome!

## Read More

- [Infrastructure as Code with Java and Pulumi](https://www.pulumi.com/blog/announcing-infrastructure-as-code-with-java-and-pulumi/) by Mikhail Shilkov
- [Unit testing Pulumi programs](https://www.pulumi.com/docs/using-pulumi/testing/unit/)
- [Integration testing for Pulumi programs](https://www.pulumi.com/docs/using-pulumi/testing/integration/)
- [Local Testing With Pulumi](https://www.pulumi.com/blog/local-testing-with-pulumi/)
- [Using Pulumi with LocalStack](https://docs.localstack.cloud/user-guide/integrations/pulumi/)
- [Pulumi and LocalStack — beyond the basics](https://delitescere.medium.com/pulumi-and-localstack-beyond-the-basics-d993f3b94d17) by Josh Graham
- [Testing AWS service integrations using LocalStack](https://testcontainers.com/guides/testing-aws-service-integrations-using-localstack/)

## License

Most of the code is licensed under the Apache License v2,
unless specified differently.

The application code is based on the [Testing AWS Service Integrations using LocalStack](https://github.com/testcontainers/tc-guide-testing-aws-service-integrations-using-localstack) demo,
and, hence, it uses the MIT License.
