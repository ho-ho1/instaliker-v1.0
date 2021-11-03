package com.instaliker;

import com.instaliker.lib.Configuration;
import com.instaliker.lib.CookiesManager;
import com.instaliker.lib.DataGenerator;
import com.instaliker.pages.HashtagPage;
import com.instaliker.pages.MyUserPage;
import com.instaliker.pages.UserPage;
import com.instaliker.pages.login.LoginPage;
import com.instaliker.pages.login.SaveLoginPage;
import com.instaliker.pages.login.TurnOnNotificationsPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Slf4j
public class InstagramIT {

    private WebDriver driver;
    private final Properties properties = Configuration.properties();

    @BeforeTest(groups = "RUNME")
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://www.instagram.com/");
        CookiesManager cookiesManager = new CookiesManager(driver);
        cookiesManager.load(new File("cookies.csv"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        SaveLoginPage saveLoginPage = new SaveLoginPage(driver);
        saveLoginPage.saveInfoIfAvailable();

        TurnOnNotificationsPage turnOnNotificationsPage = new TurnOnNotificationsPage(driver);
        turnOnNotificationsPage.notNow();
    }

    @Test(groups = "RUNME")
    public void likeHashtags() {
        UserPage myProfile = new MyUserPage(driver);

        final List<String> hashtags = myProfile.readHashtags();
        Collections.shuffle(hashtags);
        for (String hashtag : hashtags) {
            HashtagPage hashTagPage = new HashtagPage(driver, hashtag);
            hashTagPage.likeUpToPhotosWithProbablityAndDelay(
                Integer.parseInt(properties.getProperty("hashtag.photos.like.count")),
                Integer.parseInt(properties.getProperty("hashtag.photos.like.probability")));
        }
    }

    @Test
    public void likeAccountsFromConfig() {
        final String[] accounts = properties.getProperty("instagram.accounts").split(",");
        final int photosToLike = Integer.parseInt(properties.getProperty("instagram.like.photos"));

        if (accounts.length > 0) {
            for (String account : accounts) {
                UserPage instagram = new UserPage(driver, account);
                instagram.likeFirst(photosToLike);
            }
        }
    }

    @Test
    public void likeOkuniewska3() {
        UserPage okuniewska = new UserPage(driver, "tu_okuniewska");
        okuniewska.likeFirst(3);
    }

    @Test
    public void likeRenataAll() {
        UserPage renatarysuje = new UserPage(driver, "renatarysuje");
        renatarysuje.likeAllPhotos();
    }

    @Test
    public void likeFollowers() {
        UserPage myProfile = new MyUserPage(driver);

        final List<String> followers = myProfile.readFollowers();
        for (String follower : followers) {
            UserPage followerPage = new UserPage(driver, follower);
            followerPage.likeFirst(3);
        }
    }

    @Test
    public void likeFollowings() {
        UserPage myProfile = new MyUserPage(driver);

        final List<String> followings = myProfile.readFollowings();
        UserPage randomFollowing = new UserPage(driver, DataGenerator.getRandomElement(followings));
        randomFollowing.likeUpToPhotosWithProbablityAndDelay(20, 75);
    }

}
