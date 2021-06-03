package com.instaliker.pages.login;

import com.instaliker.pages.Page;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends Page {

    @FindBy(xpath = ".//input[@name='username']")
    WebElement loginText;

    @FindBy(xpath = ".//input[@name='password']")
    WebElement passwordText;

    @FindBy(xpath = ".//button[@type='submit']")
    WebElement loginButton;

    @FindBy(xpath = ".//div[@role='presentation']//button[1]")
    WebElement acceptCookiesButton;

    public LoginPage(WebDriver driver) {
        super(driver);
        driver.get("https://www.instagram.com/accounts/login/");
        PageFactory.initElements(driver, this);
    }

    public void acceptCookies() {
        wait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
        acceptCookiesButton.click();
    }

    public void login() {
        wait.until(ExpectedConditions.visibilityOf(loginText));
        loginText.sendKeys(properties.getProperty("login.username"));
        passwordText.sendKeys(decodePassword(properties.getProperty("login.password.base64")));
        wait.until(ExpectedConditions.visibilityOf(loginButton));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    private String decodePassword(String password) {
        return new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
    }
}
