package ru.ifmo.se.lab4.configuration

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import ru.ifmo.se.lab4.Lab4Application

@ContextConfiguration(
    initializers = [E2EInitializer::class]
)
@SpringBootTest(classes = [Lab4Application::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("e2e")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class E2E
