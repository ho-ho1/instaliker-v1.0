package com.instaliker.lib;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

@RequiredArgsConstructor
public class ScreenshotsManager {

    private static final String SCREENSHOTS_DIRECTORY = "./target/debug-screenshots";

    private final WebDriver driver;

    public void take(String filename) {
        copyFile(printScreen(), FilenameUtils.concat(SCREENSHOTS_DIRECTORY, filename));
    }

    private File printScreen() {
        TakesScreenshot screenshot = ((TakesScreenshot) driver);
        return screenshot.getScreenshotAs(OutputType.FILE);
    }

    private void copyFile(File source, String destinationPath) {
        String extension = FilenameUtils.getExtension(source.getName());
        destinationPath += (!extension.isEmpty() && !endsWithExtension(destinationPath)) ? "." + extension : "";

        File destination = new File(destinationPath);
        try {
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            throw new RuntimeException("Can't copy file from " + source.getAbsolutePath() + " to " + destinationPath, e);
        }
    }

    private boolean endsWithExtension(String string) {
        return string.matches(".*\\.\\w{3,4}$");
    }
}
