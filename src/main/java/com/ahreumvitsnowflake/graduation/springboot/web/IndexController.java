package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.CustomOAuth2UserService;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;

import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
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
    public String main(Model model, @LoginUser SessionUser user) {
        if(user != null){
            // 프론트에 user 정보 넘겨주기
            model.addAttribute("user", user);
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
    public String posts(Model model) {
        // 프론트에 전체 게시물 넘겨주기
        model.addAttribute("posts", postsService.findAllDesc());
        return "posts";
    }

    @GetMapping("/posts/{id}") // 특정 게시물 조회 페이지
    public String postsRead(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        PostsResponseDto dto = postsService.findById(id);
        if(user != null){
            if(!dto.getUserId().equals(user.getId())){
                postsService.updateViewCount(id);
            }
        }
        model.addAttribute("posts", dto);
        return "posts-read";
    }

    @GetMapping("/posts/update/{id}") // 특정 게시글 수정 페이지
    public String postsUpdate(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        PostsResponseDto dto = postsService.findById(id);

        if(user != null){
            if(dto.getUserId().equals(user.getId())){
                log.trace("게시글 작성자 본인입니다.");
                // 프론트에 post 정보 넘겨주기
                model.addAttribute("posts", dto);
            }
            else{
                log.trace("게시글 작성자가 아닙니다. 수정 권한은 작성자에게만 있습니다.");
                // 프론트에서 경고 알림창을 띄우거나 아니면 저 수정 버튼을 작성자 본인일 경우에만 보이도록 하기
            }
        }
        return "posts-update";
    }

    @GetMapping("/users/update/{id}") // 회원 정보(닉네임) 수정 페이지
    public String userUpdate(@PathVariable Long id, Model model){
        SessionUser dto = customOAuth2UserService.findById(id);
        
        // 프론트에 user 정보 넘겨주기
        model.addAttribute("user", dto);

        return "user-update";
    }
}
