package com.batsworks.scrapy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrapyApplication.class, args);
    }

}
