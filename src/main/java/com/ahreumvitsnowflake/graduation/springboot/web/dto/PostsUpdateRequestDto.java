package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String category;
    private String phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;

    @Builder
    public PostsUpdateRequestDto(String category, String phraseTopic, String writer, String phrase, int scrapCount, String source){
        this.category = category;
        this.phraseTopic = phraseTopic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrapCount = scrapCount;
        this.source = source;
    }
}
