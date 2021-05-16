package com.instaliker.pages;

import org.openqa.selenium.WebDriver;

public class MyUserPage extends UserPage {

    public MyUserPage(WebDriver driver) {
        super(driver, properties.getProperty("login.username"));
    }
}
