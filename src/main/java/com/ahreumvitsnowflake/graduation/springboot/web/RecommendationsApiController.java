package com.ahreumvitsnowflake.graduation.springboot.web;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.LoginUser;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.service.posts.PostsService;
import com.ahreumvitsnowflake.graduation.springboot.service.recommendations.RecommendationsService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class RecommendationsApiController {
    private final RecommendationsService recommendationsService;
    private final PostsService postsService;
    
    // 글귀 추천
    @GetMapping("/api/v1/recommendations")
    public List<PostsListResponseDto> getPostsContentsBasedRecommendations(@PageableDefault(size = 4)
                                                                           @SortDefault.SortDefaults({
                                                                                   @SortDefault(sort="scrapCount", direction = Sort.Direction.DESC),
                                                                                   @SortDefault(sort="recommendCount", direction = Sort.Direction.DESC),
                                                                                   @SortDefault(sort="id", direction = Sort.Direction.DESC)
                                                                           }) Pageable pageable, @LoginUser SessionUser user
    ) {
        List<PostsListResponseDto> recommendationsList = null;
        Pageable pageable2 = PageRequest.of(0, 4, Sort.by("recommendCount").descending().and(Sort.by("id").descending()));
        if (user != null) {
            PhraseTopic phraseTopic = recommendationsService.userScrappedPhraseTopic(user.getId());
            log.info("phraseTopic 출력 형태 : {}", phraseTopic);
            if (phraseTopic == null) {
                log.info("사용자가 선호한 phraseTopic이 없습니다.");
                recommendationsList = postsService.pageList(pageable2).getContent();
            } else {
                recommendationsList = recommendationsService.contentsBasedRecommendations(pageable, phraseTopic).getContent();
            }
        }
        else{
            recommendationsList = postsService.pageList(pageable2).getContent();
        }
        return recommendationsList;
    }
}
