package com.instaliker.pages;

import java.util.List;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserPage extends Page {

    @FindBy(xpath = ".//div[@id='react-root']//main[@role='main']//article//a[@tabindex='0']//img/parent::div/parent::div")
    List<WebElement> photos;

    @FindBy(xpath = ".//section//*[name()='svg'][@aria-label='Like']")
    List<WebElement> likeButtons;

    @FindBy(xpath = ".//section//*[name()='svg'][@aria-label='Unlike']")
    List<WebElement> unlikeButtons;

    @FindBy(xpath = ".//a[contains(@class, 'coreSpriteRightPaginationArrow')]")
    List<WebElement> nextPhotoButtons;

    @FindBy(xpath = ".//*[name()='svg'][@aria-label='Close']")
    WebElement closeButton;

    public UserPage(WebDriver driver, String instagramUsername) {
        super(driver, "https://www.instagram.com/" + instagramUsername);
    }

    public void likeFirstPhoto() {
        likePhoto(0);
    }

    public void likeFirst(int photosToLike) {
        for (int i = 0; i < photosToLike; i++) {
            likePhoto(i);
        }
    }

    private void likePhoto(int number) {
        photos.get(number).click();
        likeCurrentPhoto();
        closeButton.click();
    }

    private void likeCurrentPhoto() {
        Awaitility.await().until(() -> likeButtons.size() > 0 || unlikeButtons.size() > 0);
        if (likeButtons.size() > 0) {
            likeButtons.get(0).click();
        }
    }

    public void likeAllPhotos() {
        photos.get(0).click();
        boolean isNextPhotoAvailable;
        do {
            likeCurrentPhoto();
            isNextPhotoAvailable = nextPhotoButtons.size() > 0;
            if (isNextPhotoAvailable) {
                nextPhotoButtons.get(0).click();
            }
        } while (isNextPhotoAvailable);
        closeButton.click();
    }


}
