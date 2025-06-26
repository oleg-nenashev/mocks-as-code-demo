plugins {
    id("com.gradle.develocity") version "4.0.2"
}

rootProject.name = "workshop-aws-mocks-as-code"

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
    }
}

include("1_application")
include("2_api-mocking")
include("3_deployment")