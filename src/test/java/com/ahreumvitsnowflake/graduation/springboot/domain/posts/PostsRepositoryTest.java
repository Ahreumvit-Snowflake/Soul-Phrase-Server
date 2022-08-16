package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        String phrase_topic = "글귀 주제";
        String writer = "작성자";
        String phrase = "글귀 내용";
        String source = "출처";

        postsRepository.save(Posts.builder()
                .category(category)
                .phrase_topic(phrase_topic)
                .writer(writer)
                .phrase(phrase)
                .source(source)
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getCategory()).isEqualTo(category);
        assertThat(posts.getPhrase_topic()).isEqualTo(phrase_topic);
        assertThat(posts.getWriter()).isEqualTo(writer);
        assertThat(posts.getPhrase()).isEqualTo(phrase);
        assertThat(posts.getSource()).isEqualTo(source);
    }
}
