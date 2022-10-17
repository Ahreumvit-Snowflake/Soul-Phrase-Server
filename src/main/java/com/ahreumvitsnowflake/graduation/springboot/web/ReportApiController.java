package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.report.ReportService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.ReportRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ReportApiController {
    private final ReportService reportService;

    // post_id로 신고 등록
    @PostMapping("/api/v1/report/{postId}")
    public ResponseEntity<String> addReport(@LoginUser SessionUser user, @PathVariable Long postId, @RequestBody ReportRequestDto reqDto){
        boolean result = false;
        if (user != null) {
            result = reportService.addReport(user.getId(), postId, reqDto.getType());
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // post_id로 신고수 카운트
    @GetMapping("/api/v1/report/{postId}")
    public ResponseEntity<Integer> getReportCount(@LoginUser SessionUser user, @PathVariable Long postId){
        log.info("post-id: {}", postId);
        log.info("user: {}", user);

        Integer resultData = reportService.count(postId);

        log.info("report count: {}", resultData);
        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }
}
