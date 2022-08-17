package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String category;
    private String phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;

    @Builder
    public PostsSaveRequestDto(String category, String phraseTopic, String writer, String phrase, int scrapCount, String source){
        this.category = category;
        this.phraseTopic = phraseTopic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrapCount = scrapCount;
        this.source = source;
    }

    // dto -> entity(DB에 등록)
    public Posts toEntity(){
        return Posts.builder()
                .category(category)
                .phraseTopic(phraseTopic)
                .writer(writer)
                .phrase(phrase)
                .scrapCount(scrapCount)
                .source(source)
                .build();
    }
}
