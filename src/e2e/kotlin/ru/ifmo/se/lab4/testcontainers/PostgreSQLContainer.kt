package ru.ifmo.se.lab4.testcontainers

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object PostgreSqlContainer {
    private val postgresSqlContainer = KPostgreSqlContainer(
        DockerImageName.parse("postgres:12.7")
            .asCompatibleSubstituteFor("postgres")
    )
        .withDatabaseName("postgres")
        .withUsername("postgres")
        .withPassword("postgres")

    val jdbcUrl: String by lazy {
        postgresSqlContainer.jdbcUrl
    }

    fun startContainer() = postgresSqlContainer.start()

    private class KPostgreSqlContainer(
        dockerImageName: DockerImageName
    ) : PostgreSQLContainer<KPostgreSqlContainer>(dockerImageName)
}
