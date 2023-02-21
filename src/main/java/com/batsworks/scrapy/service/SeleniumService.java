package com.batsworks.scrapy.service;

import com.batsworks.scrapy.model.Youtube;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class SeleniumService {
    private static String dowload = System.getProperty("user.home");
    private static WebDriver driver;
    private static JavascriptExecutor js;

    public List<Youtube> getDataString(String url, long limit) throws InterruptedException {
        List<Youtube> youtubeList = new ArrayList<>();
        List<String> images = new ArrayList<>();

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions());
        js = (JavascriptExecutor) driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        driver.get("https://www.youtube.com/results?search_query=java");

        var content = driver.findElement(By.xpath("//*[@id=\"contents\"]"));
        for (var i = 1; i <= limit; i++) {
            var xpath = String.format("//*[@id=\"contents\"]/ytd-video-renderer[%s]", i);
            var video = content.findElement(By.xpath(xpath));
            var link = video.findElement(By.tagName("a"));

            var im = js.executeScript("return document.querySelectorAll('#thumbnail > yt-image > img')");
            var ims = video.findElements(By.xpath("//*[@id=\"dismissible\"]//img"));

            ims.forEach(it -> {
                System.out.println(it.getText());
                System.out.println(it.getAttribute("href"));
                System.out.println(it.getAttribute("src"));
            });

            String[] text = video.getText().split("\n");
            var tube = Youtube.builder()
                    .title(ifNull(text[0]))
                    .views(ifNull(text[1]))
                    .time(ifNull(text[2]))
                    .channel(ifNull(text[3]))
                    .description(ifNull(text[4]))
                    .videoLink(link.getAttribute("href"))
                    .build();
            youtubeList.add(tube);
        }

        driver.close();
        if (nonNull(driver))
            driver.quit();
        return youtubeList;
    }

    public static Wait<WebDriver> waitfor() {
        return new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
    }

    private static ChromeOptions chromeOptions() {
        dowload = dowload.replace("/", "\\");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions").addArguments("--disable-infobars").addArguments("--no-sandbox");
//        options.addArguments("--user-data-dir=C:\\Temp\\");
//        options.addArguments("--profile-directory=ChromeData");
        options.setHeadless(true);
        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("safebrowsing.enabled", "false");
        prefs.put("download.default_directory", dowload);
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("download.prompt_for_download", false);
        options.setExperimentalOption("prefs", prefs);
        System.err.println("## Diretório: " + dowload);
        options.setAcceptInsecureCerts(true);
        return options;
    }

    private String ifNull(String s) {
        if (nonNull(s)) {
            return s;
        } else {
            return null;
        }
    }

}