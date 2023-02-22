package com.batsworks.scrapy.configuration.doc;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class Facilities {

    public String ifNull(String s) {
        if (nonNull(s)) {
            return s;
        } else {
            return null;
        }
    }

    public void joinAndRemoveDuplicates(List<?> list, List<?> list1, List<Object> finalList) {
        list.stream().distinct().forEach(it -> {
            var index = finalList.indexOf(it);
            if (index < 0) {
                finalList.add(it);
            }
        });
        list1.stream().distinct().forEach(it -> {
            var index = finalList.indexOf(it);
            if (index < 0) {
                finalList.add(it);
            }
        });
    }
}
