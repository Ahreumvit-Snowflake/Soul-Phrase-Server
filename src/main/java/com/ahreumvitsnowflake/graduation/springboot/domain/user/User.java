package com.ahreumvitsnowflake.graduation.springboot.domain.user;

import com.ahreumvitsnowflake.graduation.springboot.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="USER")
public class User extends BaseTimeEntity {
    // user 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // 테이블 칼럼 - 사용자 이름
    @Column(nullable = false)
    private String username;

    // 테이블 칼럼 - 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 테이블 칼럼 - 프로필 사진
    private String picture;

    // 테이블 칼럼 - 사용자 권한(GUEST, USER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 테이블 칼럼 - 닉네임
    @Column(nullable = false)
    private String nickname;

    @Builder
    public User(String username, String email, String picture, Role role, String nickname){
        this.username = username;
        this.email = email;
        this.picture = picture != null ? picture : "";
        this.role = role;
        this.nickname = nickname != null ? nickname : "익명";
    }

    public User snsUpdate(String username, String picture){
        this.username = username;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
}
