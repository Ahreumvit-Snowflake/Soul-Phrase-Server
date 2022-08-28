package com.ahreumvitsnowflake.graduation.springboot.domain.scrap;

import com.ahreumvitsnowflake.graduation.springboot.domain.BaseTimeEntity;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name="SCRAP", uniqueConstraints = {
        @UniqueConstraint(
                name = "scrap_uk",
                columnNames = {"post_id", "user_id"}
        )
    }
)
public class Scrap extends BaseTimeEntity {
    // scrap 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    // scrap 테이블의 post_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts posts;

    // scrap 테이블의 user_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Scrap(Posts posts, User user){
        this.posts = posts;
        this.user = user;
    }
}
