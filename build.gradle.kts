import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip


plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
}

group = "ru.ifmo.se"
version = "0.0.1-SNAPSHOT"
val arrowVersion = "1.2.0"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.liquibase:liquibase-core")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	implementation("io.arrow-kt:arrow-core:$arrowVersion")
	implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
	implementation("io.jsonwebtoken:jjwt:0.12.3")
	implementation("org.seleniumhq.selenium:selenium-java:4.1.1")
	implementation("io.github.microutils:kotlin-logging:2.0.3")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.register<Task>("music"){
	dependsOn("build")
	doLast{
		val musicFile = File("/Users/pavelmalkov/IdeaProjects/GitHub/itmo-web-lab-4/muzlo.wav")
		val clip = AudioSystem.getClip()
		clip.open(AudioSystem.getAudioInputStream(musicFile))
		clip.start()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
