package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void postsSave_Calling() { // 게시글저장_불러오기()
        //given
        String category = "카테고리";
        String phraseTopic = "글귀 주제";
        String writer = "작성자";
        String phrase = "글귀 내용";
        String source = "출처";

        postsRepository.save(Posts.builder()
                .category(category)
                .phraseTopic(phraseTopic)
                .writer(writer)
                .phrase(phrase)
                .source(source)
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getCategory()).isEqualTo(category);
        assertThat(posts.getPhraseTopic()).isEqualTo(phraseTopic);
        assertThat(posts.getWriter()).isEqualTo(writer);
        assertThat(posts.getPhrase()).isEqualTo(phrase);
        assertThat(posts.getSource()).isEqualTo(source);
    }

    @Test
    public void baseTimeEntity_Create() { // baseTimeEntity_등록()
        //given
        LocalDateTime now = LocalDateTime.of(2022,8,17,0,0,0);
        postsRepository.save(Posts.builder()
                .category("category")
                .phraseTopic("phrase topic")
                .writer("writer")
                .phrase("phrase")
                .source("source")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>>>>>> createDate="+posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
