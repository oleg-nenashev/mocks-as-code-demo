# Mocks as Code for Configuration Management. Demo

In this demo, we build a full-Java implementation for several key aspects of an application:

- The application itself
- Test Automation code - with Testcontainers, WireMock, LocalStack and other Java tools
- Configuration Management - with [Pulumi for Java](https://www.pulumi.com/docs/languages-sdks/java/)
- All the build logic - with [Gradle Build Tool](https://gradle.org/), a.k.a Gradle

This demo is created for the Mocks as Code workshop by Oleg Nenashev.

The same demo setup can be implemented purely in Kotlin.

## Prerequisites

- Installed Pulumi app
- Testcontainers-compatible Docker Engine, preferably Docker Desktop
- Java 17

## Workshop Steps



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

## License

Most of the code is licensed under the Apache License v2,
unless specified differently.

