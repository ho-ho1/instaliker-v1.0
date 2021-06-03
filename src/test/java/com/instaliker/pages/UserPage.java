package com.instaliker.pages;

import com.instaliker.lib.DataGenerator;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class UserPage extends Page {

    private static final int MIN_THINK_TIME_SECONDS = 1;
    private static final int MAX_THINK_TIME_SECONDS = 5;

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

    private final String instagramUserName;

    public UserPage(WebDriver driver, String instagramUsername) {
        super(driver, INSTAGRAM_URL + instagramUsername);
        this.instagramUserName = instagramUsername;
    }

    public void likeFirstPhoto() {
        likePhoto(0);
    }

    public void likeFirst(int photosToLike) {
        for (int i = 0; i < photosToLike; i++) {
            try {
                likePhoto(i);
            } catch (NoSuchElementException ex) {
                log.warn("User '{}' hasn't got a photo with id: {}", instagramUserName, i);
            }
        }
    }

    private void likePhoto(int number) {
        if (number >= photos.size()) {
            throw new NoSuchElementException();
        }
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
        likeAllPhotosWithProbability(100);
    }

    public void likeUpToPhotosWithProbablity(int maxPhotosToLike, int percent) {
        likeUpToPhotosWithProbabilityAndDelay(maxPhotosToLike, percent, 0, 0);
    }

    public void likeUpToPhotosWithProbablityAndDelay(int maxPhotosToLike, int percent) {
        likeUpToPhotosWithProbabilityAndDelay(maxPhotosToLike, percent, MIN_THINK_TIME_SECONDS, MAX_THINK_TIME_SECONDS);
    }

    public void likeAllPhotosWithProbability(int percent) {
        likeAllPhotosWithProbabilityAndDelay(percent, 0, 0);
    }

    public void likeAllPhotosWithProbabilityAndDelay(int percent, int minSeconds, int maxSeconds) {
        likeUpToPhotosWithProbabilityAndDelay(-1, percent, minSeconds, maxSeconds);
    }

    public void likeUpToPhotosWithProbabilityAndDelay(int maxPhotosToLike, int percent, int minSeconds, int maxSeconds) {
        if (photos.isEmpty()) {
            log.warn("User '{}' doesn't have any photos to like", instagramUserName);
            return;
        }
        photos.get(0).click();
        boolean isNextPhotoAvailable;
        int photosLiked = 0;
        do {
            if (photosLiked >= maxPhotosToLike && photosLiked > 0) {
                break;
            }
            simulateThinkingInSeconds(minSeconds, maxSeconds);
            if (DataGenerator.getRandomPercent() < percent) {
                likeCurrentPhoto();
                photosLiked++;
            }
            isNextPhotoAvailable = nextPhotoButtons.size() > 0;
            if (isNextPhotoAvailable) {
                nextPhotoButtons.get(0).click();
            }
        } while (isNextPhotoAvailable);
        closeButton.click();
    }

    private void simulateThinkingInSeconds(int minSeconds, int maxSeconds) {
        final int thinkingInMillis = DataGenerator.getIntBetween(minSeconds * 1000, maxSeconds * 1000);
        log.debug("Think about nothing for {} ms.", thinkingInMillis);
        sleep(thinkingInMillis);
    }

    private void sleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @FindBy(xpath = ".//div[@role='dialog']//li/div[contains(@aria-labelledby, ' ')]//span/a")
    List<WebElement> peopleList;

    @FindBy(xpath = ".//a[contains(@href, '/followers/')]")
    WebElement followersButton;

    public List<String> readFollowers() {
        navigateUserPageIfNecessary();
        followersButton.click();
        return readPeopleAndClose();
    }

    @FindBy(xpath = ".//a[contains(@href, '/following/')]")
    WebElement followingButton;

    public List<String> readFollowings() {
        navigateUserPageIfNecessary();
        followingButton.click();
        return readPeopleAndClose();
    }

    private void navigateUserPageIfNecessary() {
        if (!driver.getCurrentUrl().contains(INSTAGRAM_URL + instagramUserName)) {
            driver.get(INSTAGRAM_URL + instagramUserName);
        }
    }

    private void waitForPeople() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> peopleList.size() > 0);
    }

    private void waitForHashtags() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> hashtagsList.size() > 0);
    }

    private List<String> readPeopleAndClose() {
        waitForPeople();

        final List<String> followers = peopleList
            .stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());

        closeButton.click();
        return followers;
    }

    @FindBy(xpath = ".//a[contains(@href, '/hashtag_following/')]")
    WebElement hashtagsButton;

    @FindBy(xpath = ".//div[@role='dialog']//div[contains(@aria-labelledby, ' ')]//div[string-length(@id)>0]//a")
    List<WebElement> hashtagsList;

    public List<String> readHashtags() {
        navigateUserPageIfNecessary();
        followingButton.click();
        waitForPeople();
        hashtagsButton.click();
        waitForHashtags();

        final List<String> hashtags = hashtagsList
            .stream()
            .map(WebElement::getText)
            .map(str -> str.substring(1))
            .collect(Collectors.toList());

        closeButton.click();
        return hashtags;
    }
}
