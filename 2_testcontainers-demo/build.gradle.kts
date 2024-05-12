plugins {
    `kotlin-dsl`
    `java`
    `idea`
}

repositories {
    mavenCentral()
    mavenLocal()
}

group = "mypackage"
description = "Demos for Testcontainers tests"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(enforcedPlatform("org.testcontainers:testcontainers-bom:1.19.8"))
    implementation(enforcedPlatform("software.amazon.awssdk:bom:2.21.1"))
    implementation("software.amazon.awssdk:s3")

    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:localstack")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("cloud.localstack:localstack-utils:0.2.15")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.9")
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
