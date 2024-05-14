import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import ru.ifmo.se.lab4.Lab4Application
import ru.ifmo.se.lab4.e2e.E2EInitializer

@ContextConfiguration(
    initializers = [E2EInitializer::class]
)
@SpringBootTest(classes = [Lab4Application::class], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class E2E
