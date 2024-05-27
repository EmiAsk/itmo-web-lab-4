package ru.ifmo.se.lab4.testcontainers

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration

object WebAppContainer {
    private const val ORIGINAL_PORT = 5173

    private val webAppContainer = GenericContainer("webapp")
        .withExposedPorts(5173)
        .waitingFor(
            Wait.forHttp("/")
                .forStatusCode(200)
        )
        .withStartupTimeout(Duration.ofSeconds(60))

    val url: String by lazy {
        "http://${webAppContainer.host}:${webAppContainer.getMappedPort(ORIGINAL_PORT)}"
    }

    fun startContainer() = webAppContainer.start()

    private class KPostgreSqlContainer(
        dockerImageName: DockerImageName
    ) : PostgreSQLContainer<KPostgreSqlContainer>(dockerImageName)
}
