package com.batsworks.scrapy.service;

import com.batsworks.scrapy.configuration.doc.Facilities;
import com.batsworks.scrapy.model.Youtube;
import com.batsworks.scrapy.model.YoutubePlaylist;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class SeleniumService {
    private static String dowload = System.getProperty("user.home");
    private static WebDriver driver;

    @Autowired
    private Facilities facilities;

    private void setWebDriver() {
        try {
            var chromedriver = getClass().getResource("/drivers/chromedriver.exe");
            var localdriver = chromedriver.toExternalForm();
            localdriver = localdriver.substring(localdriver.lastIndexOf("target"));
            System.setProperty("webdriver.chrome.driver", localdriver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
    }

    public List<Object> getDataString(String url, long limit) {
        List<YoutubePlaylist> playlist_list = new ArrayList<>();
        List<Youtube> video_list = new ArrayList<>();
        List<Object> all_videos = new ArrayList<>();

        setWebDriver();

        try {
            driver.get(String.format("https://www.youtube.com/results?search_query=%s", url));

            while (limit >= all_videos.size()) {
                for (var i = 1; i <= limit; i++) {
                    var content = driver.findElement(By.xpath("//*[@id=\"contents\"]"));
                    getAllVideos(content, i, video_list);
                    getAllPlaylists(content, i, playlist_list);
                }
                facilities.joinAndRemoveDuplicates(video_list, playlist_list, all_videos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            for (var i = 1; i <= limit; i++) {
                var content = driver.findElement(By.xpath("//*[@id=\"contents\"]"));
                getAllVideos(content, i, video_list);
            }
        }
        driver.close();
        if (nonNull(driver)) {
            driver.quit();
        }
        facilities.joinAndRemoveDuplicates(video_list, playlist_list, all_videos);
        return all_videos.stream().sorted().collect(Collectors.toList());
    }

    private void getAllVideos(WebElement content, int i, List<Youtube> video_list) {
        var xpath = String.format("//*[@id=\"contents\"]/ytd-video-renderer[%s]", i);
        var video = content.findElement(By.xpath(xpath));
        var link = video.findElement(By.tagName("a"));

        String[] text = video.getText().split("\n");
        var tube = Youtube.builder()
                .title(facilities.ifNull(text[0]))
                .views(facilities.ifNull(text[1])).time(facilities.ifNull(text[2]))
                .channel(facilities.ifNull(text[3]))
                .description(facilities.ifNull(text[4]))
                .videoLink(link.getAttribute("href")).build();
        video_list.add(tube);
    }

    private void getAllPlaylists(WebElement content, int i, List<YoutubePlaylist> playlist_list) {
        var xpath_playlist = String.format("//*[@id=\"contents\"]/ytd-playlist-renderer[%d]", i);
        var video_playlist = waitfor().until(ExpectedConditions.visibilityOf(content.findElement(By.xpath(xpath_playlist))));
        var link_playlist = video_playlist.findElement(By.tagName("a"));
        String[] playlistText = video_playlist.getText().split("\n");

        if (playlistText.length > 4) {
            var playList = YoutubePlaylist.builder().quantidade(playlistText[0]).title(playlistText[1]).channel(playlistText[2]).description1(playlistText[3] + "\t" + playlistText[4]).description2(playlistText[5] + "\t" + playlistText[6]).playlistlink(link_playlist.getAttribute("href")).build();
            playlist_list.add(playList);
        } else {
            var playList = YoutubePlaylist.builder().quantidade(playlistText[0]).title(playlistText[1]).channel(playlistText[2]).description1(playlistText[3]).playlistlink(link_playlist.getAttribute("href")).build();
            playlist_list.add(playList);
        }
    }

    public static Wait<WebDriver> waitfor() {
        return new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(5)).ignoring(NoSuchElementException.class);
    }

    private static ChromeOptions chromeOptions() {
        dowload = dowload.replace("/", "\\");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions").addArguments("--disable-infobars").addArguments("--no-sandbox");
//        options.addArguments("--user-data-dir=C:\\Temp\\");
//        options.addArguments("--profile-directory=ChromeData");
//        options.setHeadless(true);
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
}