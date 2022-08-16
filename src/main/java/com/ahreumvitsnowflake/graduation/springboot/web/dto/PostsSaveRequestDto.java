package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String category;
    private String phrase_topic;
    private String writer;
    private String phrase;
    private int scrap_count;
    private String source;

    @Builder
    public PostsSaveRequestDto(String category, String phrase_topic, String writer, String phrase, int scrap_count, String source){
        this.category = category;
        this.phrase_topic = phrase_topic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrap_count = scrap_count;
        this.source = source;
    }

    // dto -> entity(DB에 등록)
    public Posts toEntity(){
        return Posts.builder()
                .category(category)
                .phrase_topic(phrase_topic)
                .writer(writer)
                .phrase(phrase)
                .scrap_count(scrap_count)
                .source(source)
                .build();
    }
}
