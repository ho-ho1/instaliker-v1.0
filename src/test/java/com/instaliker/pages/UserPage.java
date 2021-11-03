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
        clickCloseButton();
    }

    private void likeCurrentPhoto() {
        Awaitility.await().until(() -> likeButtons.size() > 0 || unlikeButtons.size() > 0);
        if (unlikeButtons.size() > 0) {
            log.info("Photo liked already");
        }
        if (likeButtons.size() > 0) {
            log.info("Click 'Like' on current photo!");
            if (likeButtons.get(0).isDisplayed()) {
                likeButtons.get(0).click();
            } else if (likeButtons.get(1).isDisplayed()) {
                likeButtons.get(1).click();
            } else {
                log.warn("No 'like' buttons (out of: {}) is displayed on the page. Let's try to click that with index 1", likeButtons.size());
                likeButtons.get(1).click();
            }
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
                log.info("Max number of photos liked ({}) reached", maxPhotosToLike);
                break;
            }
            simulateThinkingInSeconds(minSeconds, maxSeconds);
            final int randomPercent = DataGenerator.getRandomPercent();
            if (randomPercent < percent) {
                log.info("I like this photo: guessed {} (threshold: {})", randomPercent, percent);
                likeCurrentPhoto();
                photosLiked++;
            } else {
                log.debug("I don't like this photo: guessed {} (threshold: {})", randomPercent, percent);
            }
            isNextPhotoAvailable = nextPhotoButtons.size() > 0;
            if (isNextPhotoAvailable) {
                log.info("Click next");
                nextPhotoButtons.get(0).click();
            }
        } while (isNextPhotoAvailable);
        clickCloseButton();
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
        clickFollowersButton();
        return readPeopleAndClose();
    }

    private void clickFollowersButton() {
        log.info("Click 'Followers' button");
        followersButton.click();
    }

    @FindBy(xpath = ".//a[contains(@href, '/following/')]")
    WebElement followingButton;

    public List<String> readFollowings() {
        navigateUserPageIfNecessary();
        clickFollowingButton();
        return readPeopleAndClose();
    }

    private void clickFollowingButton() {
        log.info("Click 'Following' button");
        followingButton.click();
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

    @FindBy(xpath = ".//div[@role='dialog']//div/span[text()='#']")
    WebElement noHashtags;

    private void waitForHashtags() {
        Awaitility.await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> hashtagsList.size() > 0 || noHashtags.isDisplayed());
    }

    private List<String> readPeopleAndClose() {
        waitForPeople();

        final List<String> followers = peopleList
            .stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
        log.info("Collected {} items", peopleList.size());
        log.debug(peopleList.toString());

        clickCloseButton();
        return followers;
    }

    private void clickCloseButton() {
        log.info("Close dialog (by X button)");
        closeButton.click();
    }

    @FindBy(xpath = ".//a[contains(@href, '/hashtag_following/')]")
    WebElement hashtagsButton;

    @FindBy(xpath = ".//div[@role='dialog']//div[contains(@aria-labelledby, ' ')]//div[string-length(@id)>0]//a")
    List<WebElement> hashtagsList;

    public List<String> readHashtags() {
        navigateUserPageIfNecessary();
        clickFollowingButton();
        waitForPeople();
        clickHashtagsButton();
        waitForHashtags();

        final List<String> hashtags = hashtagsList
            .stream()
            .map(WebElement::getText)
            .map(str -> str.substring(1))
            .collect(Collectors.toList());
        log.info("Collected {} hashtags", hashtagsList.size());
        log.debug(hashtagsList.toString());

        clickCloseButton();
        return hashtags;
    }

    private void clickHashtagsButton() {
        log.info("Click 'Hashtags'");
        hashtagsButton.click();
    }
}
