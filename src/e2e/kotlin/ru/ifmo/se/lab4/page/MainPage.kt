package ru.ifmo.se.lab4.page

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class MainPage(private val browser: WebDriver) {
    init {
        PageFactory.initElements(browser, this)
    }

    @FindBy(id = "graph")
    lateinit var graph: WebElement

    @FindBy(id = "submit-button")
    lateinit var submitBtn: WebElement

    @FindBy(id = "remove-button")
    lateinit var removeBtn: WebElement

    @FindBy(id = "logout")
    lateinit var logoutBtn: WebElement

    @FindBy(className = "table-check")
    lateinit var table: WebElement

    private val wait = WebDriverWait(browser, Duration.ofSeconds(2))

    fun selectR(r: Int) {
        val element = browser.findElement(By.id("input-r-$r"))
        wait.until(ExpectedConditions.visibilityOf(element))
        element.click()
    }

    fun selectX(x: Int) {
        val element = browser.findElement(By.id("input-x-$x"))
        wait.until(ExpectedConditions.visibilityOf(element))
        element.click()
    }

    fun inputY(y: String) {
        val element = browser.findElement(By.id("input-y"))
        wait.until(ExpectedConditions.visibilityOf(element))
        element.clear()
        element.sendKeys(y)
    }

    fun fillPointForm(r: Int, x: Int, y: String) {
        selectR(r)
        selectX(x)
        inputY(y)
    }
}
