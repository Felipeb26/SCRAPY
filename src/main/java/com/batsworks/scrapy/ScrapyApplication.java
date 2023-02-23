package com.batsworks.scrapy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapyApplication {

    public static void main(String[] args) {
        var driver = System.getProperty("user.dir").concat("/drivers/chromedriver.exe");
        System.out.println("\n\n\n" + driver + "\n\n\n");

        System.setProperty("webdriver.chrome.driver", driver);
        SpringApplication.run(ScrapyApplication.class, args);
    }

//    public static void main(String[] args) {
//        var driver = System.getProperty("user.dir").concat("/drivers/chromedriver.exe");
//        System.out.println(driver);
//    }
}
