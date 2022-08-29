package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import com.ahreumvitsnowflake.graduation.springboot.domain.BaseTimeEntity;
import com.ahreumvitsnowflake.graduation.springboot.domain.scrap.Scrap;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="POSTS")
@DynamicInsert
public class Posts extends BaseTimeEntity {
    // post 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    // 테이블 칼럼 - 카테고리(책, 영화, 드라마, 기타)
    @Enumerated(EnumType.STRING)
    @Column(length = 200, nullable = false)
    private Category category;

    // 테이블 칼럼 - 글귀 주제(사랑, 우정, 가족, 인생, 위로, 추억, 기타)
    @Enumerated(EnumType.STRING)
    @Column(length = 200, nullable = false)
    private PhraseTopic phraseTopic;

    // 테이블 칼럼 - 작성자
    @Column(nullable = false)
    private String writer;

    // 테이블 칼럼 - 글귀(내용)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String phrase;

    // 테이블 칼럼 - '스크랩' 수
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int scrapCount;

    // 테이블 칼럼 - 출처
    @Column(nullable = false)
    private String source;

    // 테이블 칼럼 - 조회수
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount;

    // user 테이블의 user_id 참조키(FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 게시글이 삭제되면 스크랩 기록도 삭제
    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Scrap> scrapList = new ArrayList<>();

    @Builder
    public Posts(Category category, PhraseTopic phraseTopic, String writer, String phrase, int scrapCount, String source, int viewCount, User user){
        this.category = category;
        this.phraseTopic = phraseTopic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrapCount = scrapCount;
        this.source = source;
        this.viewCount = viewCount;
        this.user = user;
    }

    public void update(Category category, PhraseTopic phraseTopic, String writer, String phrase, int scrapCount, String source){
        this.category = category;
        this.phraseTopic = phraseTopic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrapCount = scrapCount;
        this.source = source;
    }
}
