package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="POST")
@DynamicInsert
public class Posts {
    // post 테이블 기본키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user 테이블의 user_id 참조키(FK)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    // 테이블 칼럼 - 카테고리(책, 영화, 드라마, 기타)
    @Column(length = 200, nullable = false)
    private String category;

    // 테이블 칼럼 - 글귀 주제(사랑, 우정, 가족, 인생, 위로, 추억, 기타)
    @Column(length = 200, nullable = false)
    private String phrase_topic;

    // 테이블 칼럼 - 작성자
    @Column(nullable = false)
    private String writer;

    // 테이블 칼럼 - 글귀(내용)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String phrase;

    // 테이블 칼럼 - '스크랩' 수
    @Column(columnDefinition = "integer default 0")
    private int scrap_count;

    // 테이블 칼럼 - 출처
    @Column(nullable = false)
    private String source;

    @Builder
    public Posts(String category, String phrase_topic, String writer, String phrase, int scrap_count, String source){
        this.category = category;
        this.phrase_topic = phrase_topic;
        this.writer = writer;
        this.phrase = phrase;
        this.scrap_count = scrap_count;
        this.source = source;
    }
}
