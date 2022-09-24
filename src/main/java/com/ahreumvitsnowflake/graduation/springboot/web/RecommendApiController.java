package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.Recommend;
import com.ahreumvitsnowflake.graduation.springboot.service.recommend.RecommendService;
import com.ahreumvitsnowflake.graduation.springboot.service.scrap.ScrapService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.ScrapDto;
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

    // post_id로 추천 등록/취소 한 번에!
    @PutMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<Integer> updatePostsRecommend(@LoginUser SessionUser user, @PathVariable Long postId) {
        int result=0;
        if (user != null) {
            result= recommendService.updateRecommend(user.getId(), postId);
        }

        return result!=0 ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // post_id로 추천 등록
    @PostMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<String> addPostsRecommend(@LoginUser SessionUser user, @PathVariable Long postId){
        boolean result = false;
        if (user != null) {
            result = recommendService.addRecommend(user.getId(), postId);
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // post_id로 추천 취소
    @DeleteMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<String> cancelPostsRecommend(@LoginUser SessionUser user, @PathVariable Long postId){
        if(user != null){
            recommendService.cancelRecommend(user.getId(), postId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // post_id로 추천 수 카운트
    @GetMapping("/api/v1/recommend/{postId}")
    public ResponseEntity<List<String>> getRecommendCount(@LoginUser SessionUser user, @PathVariable Long postId){
        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        List<String> resultData = recommendService.thisPostsRecommendUserList(postId, user.getId());

        log.info("recommend count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    // 내가 추천한 글 모두 조회
    @GetMapping("/api/v1/recommend/my")
    public List<RecommendDto> myRecommendPosts(@LoginUser SessionUser user){
        return recommendService.findByUser(user.getId());
    }
}
