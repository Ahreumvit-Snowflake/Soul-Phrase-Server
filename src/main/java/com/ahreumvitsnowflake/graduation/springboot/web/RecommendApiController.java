package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.RecommendType;
import com.ahreumvitsnowflake.graduation.springboot.service.recommend.RecommendService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class RecommendApiController {
    private final RecommendService recommendService;

    @PutMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<Integer> updatePostsRecommend(@LoginUser SessionUser user, @PathVariable Long postId, @RequestBody RecommendRequestDto reqDto) {

        int result;
        if (user != null) {
            result= recommendService.updateRecommend(user.getId(), postId, reqDto.getType());
        } else result=-1;

        return result!=0 ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // post_id로 추천 수 카운트
    @GetMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<List<String>> getRecommendCount(@LoginUser SessionUser user, @PathVariable Long postId, @RequestParam("type") RecommendType type){

        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        System.out.println("type = " + type);

        List<String> resultData = recommendService.thisPostsRecommendUserList(postId, user.getId(), type);

        log.info("recommend count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    // 내가 추천한 글 모두 조회
    @GetMapping("/api/v1/recommend/my")
    public List<RecommendDto> myRecommendPosts(@LoginUser SessionUser user){
        return recommendService.myRecommendPostsList(user.getId());
    }
}
