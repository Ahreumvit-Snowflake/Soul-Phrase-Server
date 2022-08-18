package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long postId;
    // private Long userId;
    private String category;
    private String phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;

    public PostsResponseDto(Posts entity){
        this.postId = entity.getId();
        this.category = entity.getCategory();
        this.phraseTopic = entity.getPhraseTopic();
        this.writer = entity.getWriter();
        this.phrase =entity.getPhrase();
        this.scrapCount = entity.getScrapCount();
        this.source = entity.getSource();
    }
}
