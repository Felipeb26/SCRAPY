package com.batsworks.scrapy.service;

import com.batsworks.scrapy.model.Youtube;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface YoutubeService {

    List<Youtube> findMany(String url, long limit);
}
