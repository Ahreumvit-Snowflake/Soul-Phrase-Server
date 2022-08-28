package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.CustomOAuth2UserService;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;
import com.ahreumvitsnowflake.graduation.springboot.service.scrap.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ScrapApiController {
    private final ScrapService scrapService;
    private final PostsService postsService;

    @PostMapping("/api/v1/scrap/{postId}") // post_id로 스크랩 등록
    public ResponseEntity<String> addScrap(@LoginUser SessionUser user, @PathVariable Long postId){
        boolean result = false;
        if (user != null) {
            result = scrapService.addScrap(user.getId(), postId);
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/api/v1/scrap/{postId}") // post_id로 스크랩 취소
    public ResponseEntity<String> cancelScrap(@LoginUser SessionUser user, @PathVariable Long postId){
        if(user != null){
            scrapService.cancelScrap(user.getId(), postId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/scrap/{postId}") // post_id로 스크랩 수 카운트
    public ResponseEntity<List<String>> getScrapCount(@LoginUser SessionUser user, @PathVariable Long postId){
        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        List<String> resultData = scrapService.count(postId, user.getId());

        log.info("scrap count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }
}
