package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void posts_Saved() throws Exception { // posts_등록된다()
        //given
        String category = "category";
        String phrase_topic = "phrase_topic";
        String writer = "writer";
        String phrase = "phrase";
        int scrap_count = 0;
        String source = "source";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .category(category)
                .phrase_topic(phrase_topic)
                .writer(writer)
                .phrase(phrase)
                .scrap_count(scrap_count)
                .source(source)
                .build();

        String url = "http://localhost:" + port + "/api/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getCategory()).isEqualTo(category);
        assertThat(all.get(0).getPhrase_topic()).isEqualTo(phrase_topic);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
        assertThat(all.get(0).getPhrase()).isEqualTo(phrase);
        assertThat(all.get(0).getScrap_count()).isEqualTo(scrap_count);
        assertThat(all.get(0).getSource()).isEqualTo(source);
    }
}

