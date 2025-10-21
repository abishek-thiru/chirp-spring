plugins {
	id("chirp.spring-boot-app")
}

group = "com.abi"
version = "0.0.1-SNAPSHOT"

dependencies {
	implementation(projects.user)
	implementation(projects.chat)
	implementation(projects.notification)
	implementation(projects.common)

	implementation(libs.kotlin.reflect)
	implementation(libs.spring.boot.starter.security)

	implementation(libs.spring.boot.starter.data.redis)
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.amqp)
	implementation(libs.spring.boot.starter.mail)
	runtimeOnly(libs.postgresql)
}
