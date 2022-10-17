package com.ahreumvitsnowflake.graduation.springboot.domain.report;

import com.ahreumvitsnowflake.graduation.springboot.domain.BaseTimeEntity;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@Table(name="REPORT", uniqueConstraints = {
        @UniqueConstraint(
                name = "report_uk",
                columnNames = {"post_id", "user_id"}
        )
}
)
// 신고 테이블
public class Report extends BaseTimeEntity {
    // report 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    // report 테이블의 post_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Posts posts;

    // report 테이블의 user_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = STRING)
    private ReportType type;

    @Builder
    public Report(Posts posts, User user, ReportType type){
        this.posts = posts;
        this.user = user;
        this.type = type;
    }
}
