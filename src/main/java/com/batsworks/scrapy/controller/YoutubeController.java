package com.batsworks.scrapy.controller;

import com.batsworks.scrapy.service.YoutubeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Youtube METHODS")
public class YoutubeController {

    @Autowired
    private YoutubeService service;

    @GetMapping
    public ResponseEntity<List<Object>> response(@RequestParam String url, @RequestParam(defaultValue = "10") long limit) {
        return ResponseEntity.ok().body(service.findMany(url, limit));
    }

}
