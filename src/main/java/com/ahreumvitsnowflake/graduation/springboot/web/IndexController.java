package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;

import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/") // 메인 페이지
    public String main(@LoginUser SessionUser user) {
        if(user != null){
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
    public List<PostsListResponseDto> findByAll() {
        return postsService.findAllDesc();
    }

    @GetMapping("/posts/update/{id}") // 특정 게시글 수정 페이지
    public String postsUpdate(@PathVariable Long id){
        PostsResponseDto dto = postsService.findById(id);
        return "posts-update";
    }
}
