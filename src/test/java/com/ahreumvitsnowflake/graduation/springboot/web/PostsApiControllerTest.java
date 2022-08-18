package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
        String phraseTopic = "phrase_topic";
        String writer = "writer";
        String phrase = "phrase";
        int scrapCount = 0;
        String source = "source";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .category(category)
                .phraseTopic(phraseTopic)
                .writer(writer)
                .phrase(phrase)
                .scrapCount(scrapCount)
                .source(source)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getCategory()).isEqualTo(category);
        assertThat(all.get(0).getPhraseTopic()).isEqualTo(phraseTopic);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
        assertThat(all.get(0).getPhrase()).isEqualTo(phrase);
        assertThat(all.get(0).getScrapCount()).isEqualTo(scrapCount);
        assertThat(all.get(0).getSource()).isEqualTo(source);
    }

    @Test
    public void posts_Updated() throws Exception { // posts_수정된다()
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .category("category")
                .phraseTopic("phrase topic")
                .writer("writer")
                .phrase("phrase")
                .source("source")
                .build());

        Long updateId = savedPosts.getId();
        String expectedCategory = "category2";
        String expectedPhraseTopic = "phrase topic2";
        String expectedWriter = "writer2";
        String expectedPhrase = "phrase2";
        String expectedSource = "source2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .category(expectedCategory)
                .phraseTopic(expectedPhraseTopic)
                .writer(expectedWriter)
                .phrase(expectedPhrase)
                .source(expectedSource)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        //when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getCategory()).isEqualTo(expectedCategory);
        assertThat(all.get(0).getPhraseTopic()).isEqualTo(expectedPhraseTopic);
        assertThat(all.get(0).getWriter()).isEqualTo(expectedWriter);
        assertThat(all.get(0).getPhrase()).isEqualTo(expectedPhrase);
        assertThat(all.get(0).getSource()).isEqualTo(expectedSource);
    }
}

