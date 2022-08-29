package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsResponseDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PostsApiController {
    private final PostsService postsService;

    // 게시글 등록
    @PostMapping("/api/v1/posts")
    public ResponseEntity<Long> save(@RequestBody PostsSaveRequestDto requestDto, @LoginUser SessionUser user){
        return ResponseEntity.ok(postsService.save(requestDto, user.getEmail()));
    }

    // 게시글 수정
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    // post_id로 게시글 조회
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

    // post_id로 게시글 삭제
    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }

    // 내가 쓴 게시글 모두 조회
    @GetMapping("/api/v1/posts/my")
    public List<PostsListResponseDto> findByUser(@LoginUser SessionUser user){
        log.info("user id = "+user.getId());
        return postsService.findByUser(user.getId());
    }

    // 프론트 test용 코드
    @PostMapping("/api/v1/posts/test") // 게시글 등록
    public int save(@RequestBody PostsSaveRequestDto requestDto){
        System.out.println('옴');
        System.out.println("requestDto = " + requestDto);
        return 1;
    }

    @PostMapping("/api/v1/posts/test/tests") // 게시글 등록
    public int savedd(@RequestBody PostsSaveRequestDto requestDto, @LoginUser SessionUser user){
        System.out.println('옴');
        System.out.println("requestDto = " + requestDto);
        System.out.println("user = " + user);
        return 1;
    }
    @GetMapping("/test/posts/all")
    public List<PostsListResponseDto> getPostsAllTest () {
        return postsService.findAllDesc();

    }@GetMapping("/test/posts/condition")
    public List<PostsListResponseDto> getPostsConditionTest (Category category) {
        System.out.println("category = " + category);
        return postsService.findByCondition(category);
    }

    @GetMapping("/test/posts/conditions")
    public List<PostsListResponseDto> getPostsConditionsTest (Category category, PhraseTopic phraseTopic) {
        System.out.println("category = " + category);
        System.out.println("phraseTopic = " + phraseTopic);
        return postsService.findByConditions(category, phraseTopic);
    }

    @GetMapping("/api/v1/posts/all")
    public List<PostsListResponseDto> getPostsAll () {
        return postsService.findAllDesc();
    }

    @GetMapping("/api/v1/posts/condition")
    public List<PostsListResponseDto> getPostsCondition (Category category) {
        System.out.println("category = " + category);
        return postsService.findByCondition(category);
    }

    @GetMapping("/api/v1/posts/conditions")
    public List<PostsListResponseDto> getPostsConditions (Category category, PhraseTopic phraseTopic) {
        System.out.println("category = " + category);
        System.out.println("phraseTopic = " + phraseTopic);
        return postsService.findByConditions(category, phraseTopic);
    }
}
