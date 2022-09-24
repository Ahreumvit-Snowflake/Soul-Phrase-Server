package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private Category category;
    private PhraseTopic phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;

    private int recommendCount;
    private int dislikeCount;

    @Builder
    public PostsUpdateRequestDto(Category category, PhraseTopic phraseTopic, String writer, String phrase, int scrapCount, String source, int recommendCount, int dislikeCount){
        this.category = category;
        this.phraseTopic = phraseTopic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrapCount = scrapCount;
        this.source = source;
        this.recommendCount = recommendCount;
        this.dislikeCount = dislikeCount;
    }
}
