plugins {
    id("java-library")
    id("chirp.kotlin-common")
}

group = "com.abi"
version = "unspecified"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {

    api(libs.kotlin.reflect)
    api(libs.jackson.module.kotlin)
    api(libs.jackson.datatype.jsr310)

    implementation(libs.spring.boot.starter.amqp)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}