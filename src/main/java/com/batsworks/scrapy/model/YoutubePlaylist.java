package com.batsworks.scrapy.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YoutubePlaylist {

    private String quantidade;
    private String title;
    private String channel;
    private String description1;
    private String description2;
    private String playlistlink;
}
