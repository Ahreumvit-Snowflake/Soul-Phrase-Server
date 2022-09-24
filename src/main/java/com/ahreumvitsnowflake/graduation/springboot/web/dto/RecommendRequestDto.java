package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.RecommendType;
import lombok.Data;

@Data
public class RecommendRequestDto {
    RecommendType type;
}
