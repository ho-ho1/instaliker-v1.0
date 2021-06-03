package com.instaliker.pages;

import org.openqa.selenium.WebDriver;

public class HashtagPage extends UserPage {

    public HashtagPage(WebDriver driver, String hashtag) {
        super(driver, "explore/tags/" + hashtag);
    }
}
