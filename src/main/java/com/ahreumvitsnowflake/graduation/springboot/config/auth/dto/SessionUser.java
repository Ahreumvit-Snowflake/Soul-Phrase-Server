package com.ahreumvitsnowflake.graduation.springboot.config.auth.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.user.Role;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// 세션에 사용자 정보를 저장하기 위한 Dto 클래스
// 인증된 사용자 정보만 필요
@Getter
public class SessionUser implements Serializable {
    private final Long id;
    private final String username;
    private final String email;
    private final String picture;
    private final Role role;
    private final String nickname;

    public SessionUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
        this.nickname = user.getNickname();
    }
}
