package com.instaliker.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

@Slf4j
public class CookiesManager {

    final WebDriver driver;

    public CookiesManager() {
        log.warn("WebDriver not defined in CookiesManager");
        this.driver = null;
    }

    public CookiesManager(WebDriver driver) {
        this.driver = driver;
    }

    public void save(WebDriver driver, File file) {
        createFileOverriding(file);
        try (FileWriter fileWrite = new FileWriter(file);) {
            BufferedWriter bWrite = new BufferedWriter(fileWrite);
            for (Cookie ck : driver.manage().getCookies()) {
                String expiry = ck.getExpiry() == null ? "null" : ck.getExpiry().toInstant().toString();
                bWrite.write((ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";" + expiry + ";" + ck.isSecure()));
                bWrite.newLine();
            }
            bWrite.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Cookies saved: {}", driver.manage().getCookies().size());
    }

    public void load(WebDriver driver, File file) {
        int cookiesLoaded = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fileReader);
            String line;
            while ((line = bReader.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(line, ";");
                while (token.hasMoreTokens()) {
                    String name = token.nextToken();
                    String value = token.nextToken();
                    String domain = token.nextToken();
                    if (!driver.getCurrentUrl().contains(domain)) {
                        log.warn("Cookie for domain: {} doesn't match current url: {}", domain, driver.getCurrentUrl());
                        continue;
                    }
                    String path = token.nextToken();

                    Date expiry = null;
                    String val;
                    if (!(val = token.nextToken()).equals("null")) {
                        expiry = Date.from(Instant.parse(val));
                    }
                    boolean isSecure = Boolean.parseBoolean(token.nextToken());

                    Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
                    try {
                        driver.manage().addCookie(ck);
                        cookiesLoaded++;
                    } catch (Exception e) {
                        log.error(ck.toJson().toString());
                        throw e;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load a file with cookies: " + file.getAbsolutePath(), e);
        }
        log.info("Cookies loaded: {}", cookiesLoaded);
    }

    private void createFileOverriding(File file) {
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create new file with cookies: " + file.getAbsolutePath(), e);
        }
    }

    public void load(File file) {
        this.load(driver, file);
    }

    public void save(File file) {
        this.save(driver, file);
    }
}
