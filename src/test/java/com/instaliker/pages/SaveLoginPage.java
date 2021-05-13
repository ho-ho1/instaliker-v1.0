package com.instaliker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
}
