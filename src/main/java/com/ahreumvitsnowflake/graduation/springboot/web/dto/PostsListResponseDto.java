package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsListResponseDto {
    private Long postId;
    private Category category;
    private PhraseTopic phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;
    private int viewCount;
    private Long userId;
    private String modifiedDate;
    private int recommendCount;
    private int dislikeCount;

    private int reportCount;
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
        this.reportCount = entity.getReportCount();
    }
}
