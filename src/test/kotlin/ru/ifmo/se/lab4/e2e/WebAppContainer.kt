package ru.ifmo.se.lab4.e2e

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration

object WebAppContainer {
    private const val ORIGINAL_PORT = 5173

    private val webAppContainer = GenericContainer("webapp:latest")
        .withExposedPorts(5173)
        .waitingFor(
            Wait.forHttp("/")
                .forStatusCode(200)
        )
//        .withStartupCheckStrategy(
//            MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(20))
//        )
//    .waitingFor(
//        LogMessageWaitStrategy()
//    .withRegEx(".*database system is ready to accept connections.*\\s")
//    .withTimes(2)
        .withStartupTimeout(Duration.ofSeconds(60))


//    private val webAppContainer = KPostgreSqlContainer(
//        DockerImageName.parse("docker-proxy.artifactory.tcsbank.ru/postgres:12.7")
//            .asCompatibleSubstituteFor("postgres")
//    )
//        .withDatabaseName("carambola")
//        .withUsername("postgres")
//        .withPassword("postgres")
//        .withInitScript("schema.sql")

    val url: String by lazy {
        "${webAppContainer.host}:${webAppContainer.getMappedPort(ORIGINAL_PORT)}"
    }

    fun startContainer() = webAppContainer.start()

    private class KPostgreSqlContainer(
        dockerImageName: DockerImageName
    ) : PostgreSQLContainer<KPostgreSqlContainer>(dockerImageName)
}
