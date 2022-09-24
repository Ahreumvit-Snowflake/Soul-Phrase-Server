package com.ahreumvitsnowflake.graduation.springboot.domain.notrecommend;

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
@DynamicInsert
@Table(name="NOT_RECOMMEND", uniqueConstraints = {
        @UniqueConstraint(
                name = "not_recommend_uk",
                columnNames = {"post_id", "user_id"}
        )
}
)
@Entity
public class NotRecommend extends BaseTimeEntity {
    // not_recommend 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_recommend_id")
    private Long id;

    // not_recommend 테이블의 post_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts posts;

    // not_recommend 테이블의 user_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public NotRecommend(Posts posts, User user){
        this.posts = posts;
        this.user = user;
    }
}
