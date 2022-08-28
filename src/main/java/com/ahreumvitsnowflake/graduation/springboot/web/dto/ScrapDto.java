package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.scrap.Scrap;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapDto {
    private Long id;
    private Long postId;
    private Long userId;

    public ScrapDto(Scrap entity){
        this.id = entity.getId();
        this.postId = entity.getPosts().getId();
        this.userId = entity.getUser().getId();
    }
}
