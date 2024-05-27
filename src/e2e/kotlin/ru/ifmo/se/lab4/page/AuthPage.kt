package ru.ifmo.se.lab4.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration


class AuthPage(val browser: WebDriver) {
    init {
        PageFactory.initElements(browser, this)
    }

    @FindBy(xpath = "//*[@id=\"loginButton\"]")
    lateinit var loginBtn: WebElement

    @FindBy(xpath = "//*[@id=\"buttons\"]/button[2]")
    lateinit var registerBtn: WebElement

    @FindBy(id = "emailInput")
    lateinit var emailInput: WebElement

    @FindBy(id = "passwordInput")
    lateinit var passwordInput: WebElement

    fun fillAuthForm(login: String, password: String) {
        val wait = WebDriverWait(browser, Duration.ofSeconds(2))
        wait.until(ExpectedConditions.visibilityOf(emailInput))
        wait.until(ExpectedConditions.visibilityOf(passwordInput))
        emailInput.clear()
        passwordInput.clear()
        emailInput.sendKeys(login)
        passwordInput.sendKeys(password)
    }
}
