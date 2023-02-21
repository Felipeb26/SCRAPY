package com.batsworks.scrapy.configuration.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Documentation {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("BATSWORKS YouTube")
                        .description("API to get simple data from youtube")
                        .contact(contact()));
    }

    public Contact contact() {
        return new Contact()
                .name("Felipe Batista da Silva")
                .email("felipeb2silva@gmail.com");
    }
}
