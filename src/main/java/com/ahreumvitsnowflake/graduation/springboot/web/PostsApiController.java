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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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
    public PostsResponseDto findById(@PathVariable Long id, @LoginUser SessionUser user){
        PostsResponseDto dto = postsService.findById(id);
        if(user!=null){
            if(!dto.getUserId().equals(user.getId())){
                postsService.updateViewCount(id);
            }
        }
        return dto;
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

    // 게시글 전체 조회
    @GetMapping("/api/v1/posts/all")
    public List<PostsListResponseDto> getPostsAll () {
        return postsService.findAllDesc();
    }

    // 게시글 30개씩 조회
    @GetMapping("/api/v1/posts/paging")
    public Page<PostsListResponseDto> getPostsWithPaging (@PageableDefault(size=30, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postsService.pageList(pageable);
    }

    // 최신순 20개씩 조회 - category 선택 포함
    @GetMapping("/api/v1/posts/condition")
    public Page<PostsListResponseDto> getPostsCondition (@PageableDefault(size=20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Category category) {
        log.info("category : {}", category);
        return postsService.findByCondition(pageable, category);
    }

    // 카테고리별/주제별 최신순 20개씩 조회 API
    @GetMapping("/api/v1/posts/order/recent")
    public Page<PostsListResponseDto> getPostsConditions (@PageableDefault(size=20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Category category, PhraseTopic phraseTopic) {
        log.info("category : {}, phraseTopic : {}", category, phraseTopic);
        return postsService.findByConditions(pageable, category, phraseTopic);
    }

    // 카테고리별/주제별 스크랩순 20개씩 조회 API
    @GetMapping("/api/v1/posts/order/scrap")
    public Page<PostsListResponseDto> getPostsOrderByScrap(@PageableDefault(size = 20)
                                                               @SortDefault.SortDefaults({
                                                                       @SortDefault(sort="scrapCount", direction = Sort.Direction.DESC),
                                                                       @SortDefault(sort="id", direction = Sort.Direction.DESC)
                                                               }) Pageable pageable,
                                                           Category category, PhraseTopic phraseTopic){
        log.info("category : {}, phraseTopic : {}", category, phraseTopic);
        return postsService.findByConditions(pageable, category, phraseTopic);
    }

    // 일주일 간 인기글 Top 10 조회
    @GetMapping("/api/v1/posts/popular/week")
    public List<PostsListResponseDto> getPostsOrderByPopular(@PageableDefault()
                                                             @SortDefault.SortDefaults({
                                                                     @SortDefault(sort="scrapCount", direction = Sort.Direction.DESC),
                                                                     @SortDefault(sort="recommendCount", direction = Sort.Direction.DESC),
                                                                     @SortDefault(sort="id", direction = Sort.Direction.DESC)
                                                             }) Pageable pageable
                                                             ){
        Slice<PostsListResponseDto> popularPosts = postsService.popularPosts(pageable);
        return popularPosts.getContent();
    }
}
