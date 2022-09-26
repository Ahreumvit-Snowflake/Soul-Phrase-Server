package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.notrecommend.NotRecommendService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.NotRecommendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class NotRecommendApiController {
    private final NotRecommendService notRecommendService;

    // post_id로 비추천 등록
    @PostMapping("/api/v1/not-recommend/{postId}")
    public ResponseEntity<String> addPostsNotRecommend(@LoginUser SessionUser user, @PathVariable Long postId){
        boolean result = false;
        if (user != null) {
            result = notRecommendService.addNotRecommend(user.getId(), postId);
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // post_id로 비추천 취소
    @DeleteMapping("/api/v1/not-recommend/{postId}")
    public ResponseEntity<String> cancelPostsNotRecommend(@LoginUser SessionUser user, @PathVariable Long postId){
        if(user != null){
            notRecommendService.cancelNotRecommend(user.getId(), postId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // post_id로 비추천 수 카운트
    @GetMapping("/api/v1/not-recommend/{postId}")
    public ResponseEntity<List<String>> getRecommendCount(@LoginUser SessionUser user, @PathVariable Long postId){
        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        List<String> resultData = notRecommendService.thisPostsNotRecommendUserList(postId, user.getId());

        log.info("recommend count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    // 내가 비추천한 글 모두 조회
    @GetMapping("/api/v1/not-recommend/my")
    public List<NotRecommendDto> myNotRecommendPosts(@LoginUser SessionUser user){
        return notRecommendService.findByUser(user.getId());
    }
}
