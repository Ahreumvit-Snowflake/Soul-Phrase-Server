package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.CustomOAuth2UserService;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;

import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class IndexController {
    private final PostsService postsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/") // 메인 페이지
    public String main(@LoginUser SessionUser user) {
        if(user != null){
            // 프론트에 user 정보 넘겨주기
            // model.addAttribute("user", user);
            return "main "+user.getUsername();
        }
        else {
            return "main";
        }
    }

    @GetMapping("/posts/save") // 게시글 등록 버튼 누른 후 이동할 페이지
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts") // 전체 게시글 조회 페이지
    public String posts() {
        // 프론트에 전체 게시물 넘겨주기
        // model.addAttribute("posts", postsService.findAllDesc());
        return "posts";
    }

    @GetMapping("/posts/update/{id}") // 특정 게시글 수정 페이지
    public String postsUpdate(@PathVariable Long id, @LoginUser SessionUser user){
        PostsResponseDto dto = postsService.findById(id);
        
        // 프론트에 post 정보 넘겨주기
        // model.addAttribute("post", dto);
        
        if(user != null){
            if(dto.getUserId().equals(user.getId())){
                log.trace("게시글 작성자 본인임");
            }
        }
        return "posts-update";
    }

    @GetMapping("/users/update/{id}") // 회원 정보(닉네임) 수정 페이지
    public String userUpdate(@PathVariable Long id){
        SessionUser dto = customOAuth2UserService.findById(id);
        
        // 프론트에 user 정보 넘겨주기
        // model.addAttribute("user", dto);

        return "user-update";
    }
}
