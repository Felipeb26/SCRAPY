package com.batsworks.scrapy.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Youtube {
    private String title;
    private String views;
    private String time;
    private String channel;
    private String description;
    private String videoLink;
}
