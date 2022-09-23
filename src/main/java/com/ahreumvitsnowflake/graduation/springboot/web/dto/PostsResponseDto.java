package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private final Long postId;
    private final Category category;
    private final PhraseTopic phraseTopic;
    private final String writer;
    private final String phrase;
    private final int scrapCount;
    private final String source;
    private final int viewCount;
    private final Long userId;
    private final int recommendCount;

    public PostsResponseDto(Posts entity){
        this.postId = entity.getId();
        this.category = entity.getCategory();
        this.phraseTopic = entity.getPhraseTopic();
        this.writer = entity.getWriter();
        this.phrase =entity.getPhrase();
        this.scrapCount = entity.getScrapCount();
        this.source = entity.getSource();
        this.viewCount = entity.getViewCount();
        this.userId = entity.getUser().getId();
        this.recommendCount = entity.getRecommendCount();
    }
}
