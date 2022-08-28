package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.CustomOAuth2UserService;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/api/v1/users/{id}") // user_id로 회원정보조회
    public SessionUser findById(@PathVariable Long id){
        return customOAuth2UserService.findById(id);
    }

    @PutMapping("/api/v1/users/{id}") // user_id로 회원정보(닉네임) 수정
    public Long update(@PathVariable Long id, @RequestBody SessionUser requestDto){
        return customOAuth2UserService.updateNickname(id, requestDto);
    }

    @DeleteMapping("/api/v1/users/{id}") // user_id로 회원탈퇴
    public Long delete(@PathVariable Long id){
        customOAuth2UserService.delete(id);
        log.info("탈퇴되는 회원의 아이디 : {}", id);
        return id;
    }

    @GetMapping("/login-check")
    public boolean ifLogin (@LoginUser SessionUser user) {
        System.out.println("user = " + user);
        if (null == user) return false;
        SessionUser byId = customOAuth2UserService.findById(user.getId());
        if (null != byId) {
            return true;
        }
        else {
            return false;
        }
    }
}
