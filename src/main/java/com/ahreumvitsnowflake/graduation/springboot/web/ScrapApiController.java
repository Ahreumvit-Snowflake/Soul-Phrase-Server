package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.scrap.ScrapService;
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
public class ScrapApiController {
    private final ScrapService scrapService;

    // post_id로 스크랩 등록
    @PostMapping("/api/v1/scrap/{postId}")
    public ResponseEntity<String> addScrap(@LoginUser SessionUser user, @PathVariable Long postId){
        boolean result = false;
        if (user != null) {
            result = scrapService.addScrap(user.getId(), postId);
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // post_id로 스크랩 취소
    @DeleteMapping("/api/v1/scrap/{postId}")
    public ResponseEntity<String> cancelScrap(@LoginUser SessionUser user, @PathVariable Long postId){
        if(user != null){
            scrapService.cancelScrap(user.getId(), postId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // post_id로 스크랩 수 카운트
    @GetMapping("/api/v1/scrap/{postId}")
    public ResponseEntity<List<String>> getScrapCount(@LoginUser SessionUser user, @PathVariable Long postId){
        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        List<String> resultData = scrapService.count(postId, user.getId());

        log.info("scrap count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    // 내가 스크랩한 글 모두 조회
    @GetMapping("/api/v1/scrap/my")
    public List<ScrapDto> findByUser(@LoginUser SessionUser user){
        return scrapService.findByUser(user.getId());
    }
}
