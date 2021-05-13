package com.instaliker;

import com.instaliker.lib.Configuration;
import com.instaliker.lib.CookiesManager;
import com.instaliker.pages.LoginPage;
import com.instaliker.pages.SaveLoginPage;
import com.instaliker.pages.TurnOnNotificationsPage;
import com.instaliker.pages.UserPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
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

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        CookiesManager cookiesManager = new CookiesManager(driver);
        cookiesManager.load(new File("cookies.csv"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login();

        SaveLoginPage saveLoginPage = new SaveLoginPage(driver);
        saveLoginPage.saveInfo();

        TurnOnNotificationsPage turnOnNotificationsPage = new TurnOnNotificationsPage(driver);
        turnOnNotificationsPage.notNow();
    }

    @Test(groups = "RUNME")
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

}
