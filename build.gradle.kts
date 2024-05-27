import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.utils.join
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.sound.sampled.AudioSystem


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
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
    implementation("io.jsonwebtoken:jjwt:0.12.3")
    implementation("org.seleniumhq.selenium:selenium-java:4.1.1")
    implementation("io.github.microutils:kotlin-logging:2.0.3")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.mockito:mockito-inline:4.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class)

        val e2e by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure {
                        systemProperty("SPRING_PROFILES_ACTIVE", "e2e")
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

val e2eImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

tasks.build {
    dependsOn("e2e")
}


tasks.register<Task>("music") {
    dependsOn(tasks.build)
    doLast {
        val musicFile = File("./assets/muzlo.wav")
        val clip = AudioSystem.getClip()
        clip.open(AudioSystem.getAudioInputStream(musicFile))
        clip.start()
    }
}

tasks.register<Task>("diff") {
    val exitCode = exec {
        workingDir = projectDir
        isIgnoreExitValue = true
        standardOutput = System.out
        val files = BufferedReader(InputStreamReader(FileInputStream(File("./assets/files.txt")))).readLines()
        if (files.isNotEmpty()) commandLine(
            "git",
            "diff",
            "--diff-filter=MRD",
            "--name-only",
            "--exit-code",
            join(files, " ")
        )
    }

    when (exitCode.exitValue) {
        1 -> {
            println("Changes detected in relevant classes. Commiting to git...")
            exec {
                workingDir = projectDir
                standardOutput = System.out
                commandLine("git", "add", "--all")
            }
            exec {
                workingDir = projectDir
                standardOutput = System.out
                commandLine("git", "commit", "-m", "'chore: update generated code'")
            }
        }

        0 -> println("No changes to relevant classes. Skip commiting")

        else -> println("Something went wrong")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
