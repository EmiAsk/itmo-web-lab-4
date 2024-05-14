package ru.ifmo.se.lab4.e2e

import E2E
import mu.KotlinLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions


val log = KotlinLogging.logger { }

@E2E
class WebAppE2ETest {
    val baseUrl = "http://localhost:8080"
//
//    private var addPage: AddPage? = null
//    private var detailPage: DetailPage? = null
//    private var homePage: HomePage? = null

    @BeforeEach
    fun beforeEach() {
//        browser.manage().deleteAllCookies();
//        addPage = AddPage(browser)
//        detailPage = DetailPage(browser)
//        homePage = HomePage(browser)
//        browser.get(baseUrl)
    }

    @Test
    fun homePageNoResults() {
        log.error { WebAppContainer.url }
        browser.get("http://${WebAppContainer.url}")
        while (true) {

        }
    }
//
//    @Test
//    @Order(2)
//    @DisplayName("I see 'Add Product' when I click on the 'Add Product' link")
//    fun titleOfAddPage() {
//        homePage.addLink.click()
//        assertThat(addPage.h1.getText()).isEqualTo("Add Product")
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("I want to add a new Product and see its details page")
//    fun addNewProduct() {
//        homePage.addLink.click()
//        addPage.fillFormWithProduct(Product(-1, "Beer", 0.33, "L", 0.5, "€", Date(1642523079644L)))
//        addPage.submit.click()
//        assertThat(detailPage.h1.getText()).isEqualTo("Product Details")
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("I see 'Product Details' when I click on the 'Details' link for the first Product")
//    fun detailPage() {
//        homePage.getDetailLink().click()
//        assertThat(detailPage.h1.getText()).isEqualTo("Product Details")
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("I see 'Edit Product' when I click on the 'Edit' link for the first Product")
//    fun editPage() {
//        homePage.getEditLink().click()
//        assertThat(addPage.h1.getText()).isEqualTo("Edit Product")
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("I see 'Delete Product Beer' when I click on the 'Delete' link for the first Product")
//    fun deletePage() {
//        homePage.getDeleteLink().click()
//        assertThat(homePage.h1.getText()).isEqualTo("Delete Product Beer")
//    }

    companion object {
        lateinit var browser: WebDriver

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            browser = ChromeDriver(ChromeOptions().addArguments())
            browser.get("https://www.youtube.com/")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
//            browser.quit()
        }
    }
}
