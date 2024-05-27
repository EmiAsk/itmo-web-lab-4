package ru.ifmo.se.lab4

import mu.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertAll
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Actions
import ru.ifmo.se.lab4.configuration.E2E
import ru.ifmo.se.lab4.page.AuthPage
import ru.ifmo.se.lab4.page.MainPage
import ru.ifmo.se.lab4.testcontainers.WebAppContainer
import java.time.Duration


val log = KotlinLogging.logger { }

@E2E
@TestMethodOrder(OrderAnnotation::class)
class WebAppE2ETest {

    @BeforeEach
    fun setUp() {
        Thread.sleep(3000)
    }

    @Test
    @DisplayName("Регистрация и вход в аккаунт. Некорректный email")
    @Order(1)
    fun `login not successful due to invalid mail`() {
        browser.get("${WebAppContainer.url}/$AUTH_ENDPOINT")

        // act
        authPage.fillAuthForm("invalid email", "password")

        // assert
        assert(!authPage.registerBtn.isEnabled)
        assert(!authPage.loginBtn.isEnabled)
    }

    @Test
    @DisplayName("Регистрация и вход в аккаунт. Пользователь не существует")
    @Order(2)
    fun `login, user not existed`() {
        browser.get("${WebAppContainer.url}/$AUTH_ENDPOINT")

        // act
        authPage.fillAuthForm("admin@mail.ru", "password")
        authPage.loginBtn.click()

        // assert
        assert(browser.currentUrl.replace("//", "",).split("/", limit = 2)[1] == AUTH_ENDPOINT)
    }

    @Test
    @DisplayName("Регистрация и вход в аккаунт. Неверный пароль")
    @Order(3)
    fun `login, invalid password`() {
        browser.get("${WebAppContainer.url}/$AUTH_ENDPOINT")

        // act
        authPage.fillAuthForm("invalidpassword@mail.ru", "password")
        authPage.registerBtn.click()

        authPage.fillAuthForm("invalidpassword@mail.ru", "password2")
        authPage.loginBtn.click()

        Thread.sleep(500)
        val i = 0
        // assert
        assert(browser.currentUrl.replace("//", "").split("/", limit = 2)[1] == AUTH_ENDPOINT)
    }

    @Test
    @DisplayName("Регистрация и вход в аккаунт. Успех")
    @Order(4)
    fun `login, success`() {
        browser.get("${WebAppContainer.url}/$AUTH_ENDPOINT")
        authPage.fillAuthForm("admin@mail.ru", "password")
        authPage.registerBtn.click()
        Thread.sleep(1000)
        authPage.loginBtn.click()
    }

    @Test
    @DisplayName("Добавление точки через заполнение формы. Некорректные координаты Y")
    @Order(5)
    fun `add point by filling form, invalid Y coordinate`() {
        // act
        mainPage.fillPointForm(2, 1, "invalid y")
        mainPage.submitBtn.click()

        val i = 0
        // assert
        val points = mainPage.graph.findElements(By.tagName("circle"))
        val element = browser.findElement(By.className("container-table"))

        assertAll(
            {
                assert(points.size == 0)
            },
            {
                assert(element.text.contains("Точек нет"))
            }
        )
    }

    @Test
    @DisplayName("Добавление точки через заполнение формы. Некорректные границы координаты Y")
    @Order(6)
    fun `add point by filling form, invalid Y coordinate bounds`() {
        // act
        mainPage.fillPointForm(2, 1, "4")
        mainPage.submitBtn.click()

        val i = 0

        // assert
        val points = mainPage.graph.findElements(By.tagName("circle"))
        val element = browser.findElement(By.className("container-table"))

        assertAll(
            {
                assert(points.size == 0)
            },
            {
                assert(element.text.contains("Точек нет"))
            }
        )
    }

    @Test
    @DisplayName("Добавление точки через заполнение формы. Успех")
    @Order(7)
    fun `add point by filling form`() {
        // act
        mainPage.fillPointForm(2, 1, "0")
        mainPage.submitBtn.click()

        Thread.sleep(1000)

        // assert
        val points = mainPage.graph.findElements(By.tagName("circle"))
        assert(points.size == 1)
        assert(points[0].getAttribute("fill") == "red")

        val rows = mainPage.table.findElements(By.className("table-row"))
        assert(rows.size == 1)
        val cols = rows[0].findElements(By.tagName("td"))
        assert(cols[2].text == "2")
        assert(cols[4].text == "✗")
    }

    @Test
    @DisplayName("Добавление точки нажатием на график. Успех")
    @Order(8)
    fun `add point by clicking on graph, success`() {
        // act
        mainPage.selectR(2)
        val builder = Actions(browser)
        builder.moveToElement(mainPage.graph, -10, -10).click().build().perform()

        Thread.sleep(1000)

        // assert
        val points = mainPage.graph.findElements(By.tagName("circle"))
        val rows = mainPage.table.findElements(By.className("table-row"))
        val cols = rows[0].findElements(By.tagName("td"))

        assertAll(
            {
                assert(points.size == 2)
                assert(points[0].getAttribute("fill") == "green")
            },
            {
                assert(rows.size == 2)
                assert(cols[2].text == "2")
                assert(cols[4].text == "✓")
            }
        )
    }

    @Test
    @DisplayName("Удаление всех точек. Успех")
    @Order(9)
    fun `remove all points`() {
        // act
        mainPage.removeBtn.click()

        Thread.sleep(1000)

        // assert
        val points = mainPage.graph.findElements(By.tagName("circle"))
        assert(points.size == 0)
        val element = browser.findElement(By.className("container-table"))
        assert(element.text.contains("Точек нет"))
    }

    companion object {
        const val AUTH_ENDPOINT = "~s367064/auth"

        private lateinit var browser: WebDriver
        private lateinit var mainPage: MainPage
        private lateinit var authPage: AuthPage

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            browser = ChromeDriver(ChromeOptions().addArguments())
            browser.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            browser.manage().window().maximize()
            mainPage = MainPage(browser)
            authPage = AuthPage(browser)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            browser.quit()
        }
    }
}
