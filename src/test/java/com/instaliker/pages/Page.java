package com.instaliker.pages;

import com.instaliker.lib.Configuration;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Page {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static Properties properties = Configuration.properties();

    public Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 30);
    }

    public Page(WebDriver driver, String page) {
        this(driver);
        driver.get(page);
        PageFactory.initElements(driver, this);
    }

    public Page(WebDriver driver, By pageLoadedIndicator) {
        this(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageLoadedIndicator));
        PageFactory.initElements(driver, this);
    }
}
