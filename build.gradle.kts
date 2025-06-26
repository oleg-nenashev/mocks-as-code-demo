
plugins {
    id("java")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    targetCompatibility = "21"
    sourceCompatibility = "21"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
