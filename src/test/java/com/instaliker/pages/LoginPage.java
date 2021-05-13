package com.instaliker.pages;

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
        wait.until(ExpectedConditions.visibilityOf(acceptCookiesButton));
        acceptCookiesButton.click();
    }

    public void login() {
        wait.until(ExpectedConditions.visibilityOf(loginText));
        loginText.sendKeys(properties.getProperty("login.username"));
        passwordText.sendKeys(properties.getProperty("login.password"));
        loginButton.click();
    }
}
