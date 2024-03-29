package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mvc;
    
    @Before // 매번 테스트가 시작되기 전에 MockMvc 인스턴스 생성
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER") // ROLE_USER 권한 가진 사용자가 API 요청하는 것과 동일한 효과
    public void posts_Saved() throws Exception { // posts_등록된다()
        //given
        Category category = Category.BOOK;
        PhraseTopic phraseTopic = PhraseTopic.LIFE;
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

        // when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getCategory()).isEqualTo(category);
        assertThat(all.get(0).getPhraseTopic()).isEqualTo(phraseTopic);
        assertThat(all.get(0).getWriter()).isEqualTo(writer);
        assertThat(all.get(0).getPhrase()).isEqualTo(phrase);
        assertThat(all.get(0).getScrapCount()).isEqualTo(scrapCount);
        assertThat(all.get(0).getSource()).isEqualTo(source);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void posts_Updated() throws Exception { // posts_수정된다()
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .category(Category.BOOK)
                .phraseTopic(PhraseTopic.LIFE)
                .writer("writer")
                .phrase("phrase")
                .source("source")
                .build());

        Long updateId = savedPosts.getId();
        Category expectedCategory = Category.DRAMA;
        PhraseTopic expectedPhraseTopic = PhraseTopic.FAMILY;
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

        // when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getCategory()).isEqualTo(expectedCategory);
        assertThat(all.get(0).getPhraseTopic()).isEqualTo(expectedPhraseTopic);
        assertThat(all.get(0).getWriter()).isEqualTo(expectedWriter);
        assertThat(all.get(0).getPhrase()).isEqualTo(expectedPhrase);
        assertThat(all.get(0).getSource()).isEqualTo(expectedSource);
    }
}

