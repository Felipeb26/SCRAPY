package com.batsworks.scrapy.service.implement;


import com.batsworks.scrapy.model.Youtube;
import com.batsworks.scrapy.service.SeleniumService;
import com.batsworks.scrapy.service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YoutubeImplements implements YoutubeService {

    @Autowired
    private SeleniumService service;

    @Override
    public List<Youtube> findMany(String url, long limit) {
        try {
            url = url.replace(" ", "+");
            return service.getDataString(url, limit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
