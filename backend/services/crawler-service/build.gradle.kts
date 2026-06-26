plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.reflect)

    // Spring
    implementation(libs.spring.boot.starter)

    // Logging
    implementation(libs.kotlin.logging)

    // Tests
    testImplementation(libs.spring.boot.starter.test)
}
