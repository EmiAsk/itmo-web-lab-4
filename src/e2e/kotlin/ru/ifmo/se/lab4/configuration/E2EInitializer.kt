package ru.ifmo.se.lab4.configuration

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import ru.ifmo.se.lab4.testcontainers.PostgreSqlContainer
import ru.ifmo.se.lab4.testcontainers.WebAppContainer

class E2EInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        initWebApp()
        initDatasource(applicationContext)
    }

    private fun initWebApp() {
        WebAppContainer.startContainer()
    }

    private fun initDatasource(applicationContext: ConfigurableApplicationContext) {
        PostgreSqlContainer.startContainer()

        TestPropertyValues.of(
            "spring.datasource.url=" + PostgreSqlContainer.jdbcUrl
        )
            .applyTo(applicationContext.environment)
    }
}
