package com.instaliker.pages.login;

import com.instaliker.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SaveLoginPage extends Page {

    protected static final By pageLoadBy = By.xpath(".//main[@role='main']//section//button[@type='button']");

    @FindBy(xpath = ".//main[@role='main']//section//button[@type='button']")
    WebElement saveInfoButton;

    public SaveLoginPage(WebDriver driver) {
        super(driver, pageLoadBy);
    }

    public void saveInfo() {
        saveInfoButton.click();
    }

    public void saveInfoIfAvailable() {
        if (saveInfoButton.isDisplayed()) {
            saveInfoButton.click();
        }
    }
}
