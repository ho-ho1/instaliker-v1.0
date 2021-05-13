package com.instaliker.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TurnOnNotificationsPage extends Page {

    private static final String pageLoadedXpath = ".//body//div[@role='presentation']/div[@role='dialog']//button[2]";
    private static final By pageLoadedIndicator = By.xpath(pageLoadedXpath);

    @FindBy(xpath = pageLoadedXpath)
    WebElement notNowButton;

    public TurnOnNotificationsPage(WebDriver driver) {
        super(driver, pageLoadedIndicator);
    }

    public void notNow() {
        notNowButton.click();
    }
}
