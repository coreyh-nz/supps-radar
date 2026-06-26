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

    // Database
    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.spring.boot4.starter)
    implementation(libs.postgresql)

    // Logging
    implementation(libs.kotlin.logging)

    // Internal Libraries
    implementation(project(":libraries:common"))
    implementation(project(":libraries:persistence"))

    // Tests
    testImplementation(libs.spring.boot.starter.test)
}
