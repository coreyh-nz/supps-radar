plugins {
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    api(platform(libs.exposed.bom))
    api(libs.exposed.core)
    api(libs.exposed.kotlin.datetime)
}
