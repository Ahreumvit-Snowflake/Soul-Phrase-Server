package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.scrap.Scrap;
import lombok.*;

@Getter
@NoArgsConstructor
public class ScrapDto {
    private Long scrapId;
    private Long postId;
    private Category category;
    private PhraseTopic phraseTopic;
    private String writer;
    private String phrase;
    private int scrapCount;
    private String source;
    private int viewCount;
    private Long writerId;
    private String postsModifiedDate;
    private int recommendCount;

    public ScrapDto(Scrap entity){
        this.scrapId = entity.getId();
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
