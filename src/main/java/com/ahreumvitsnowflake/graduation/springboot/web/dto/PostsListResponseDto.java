package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsListResponseDto {
    private final Long postId;
    private final Category category;
    private final PhraseTopic phraseTopic;
    private final String writer;
    private final String phrase;
    private final int scrapCount;
    private final String source;
    private final int viewCount;
    private final Long userId;
    private final String modifiedDate;
    private final int recommendCount;
    private final int dislikeCount;

    public PostsListResponseDto(Posts entity){
        this.postId = entity.getId();
        this.category = entity.getCategory();
        this.phraseTopic = entity.getPhraseTopic();
        this.writer = entity.getUser().getNickname();
        this.phrase = entity.getPhrase();
        this.scrapCount = entity.getScrapCount();
        this.source = entity.getSource();
        this.viewCount = entity.getViewCount();
        this.userId = entity.getUser().getId();
        this.modifiedDate = entity.getModifiedDate();
        this.recommendCount = entity.getRecommendCount();
        this.dislikeCount = entity.getDislikeCount();
    }
}
