package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.scrap.Scrap;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScrapDto {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime createdDate;

    public ScrapDto(Scrap entity){
        this.id = entity.getId();
        this.postId = entity.getPosts().getId();
        this.userId = entity.getUser().getId();
        this.createdDate = entity.getCreatedDate();
    }
}
