package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.notrecommend.NotRecommend;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotRecommendDto {
    private Long notRecommendId;
    private Long postId;
    private Category category;
    private PhraseTopic phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;
    private int viewCount;
    private int recommendCount;
    private Long writerId;
    private String postsModifiedDate;

    public NotRecommendDto(NotRecommend entity){
        this.notRecommendId = entity.getId();
        this.postId = entity.getPosts().getId();
        this.category = entity.getPosts().getCategory();
        this.phraseTopic = entity.getPosts().getPhraseTopic();
        this.writer = entity.getUser().getNickname();
        this.phrase = entity.getPosts().getPhrase();
        this.scrapCount = entity.getPosts().getScrapCount();
        this.source = entity.getPosts().getSource();
        this.viewCount = entity.getPosts().getViewCount();
        this.recommendCount = entity.getPosts().getRecommendCount();
        this.writerId = entity.getPosts().getUser().getId();
        this.postsModifiedDate = entity.getPosts().getModifiedDate();
    }
}
