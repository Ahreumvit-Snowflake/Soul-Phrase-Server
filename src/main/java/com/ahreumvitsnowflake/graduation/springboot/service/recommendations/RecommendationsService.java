package com.ahreumvitsnowflake.graduation.springboot.service.recommendations;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import com.ahreumvitsnowflake.graduation.springboot.service.recommend.RecommendService;
import com.ahreumvitsnowflake.graduation.springboot.service.scrap.ScrapService;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.ScrapDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecommendationsService {
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final ScrapService scrapService;
    private final RecommendService recommendService;

    // 사용자가 가장 많이 스크랩한 글귀의 phraseTopic 반환
    @Transactional(readOnly = true)
    public PhraseTopic userScrappedPhraseTopic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 아이디가 없습니다. id="+userId));
        List<ScrapDto> scrapPosts = scrapService.findByUser(userId);
        List<RecommendDto> recommendPosts = recommendService.myRecommendPostsList(userId);
        ArrayList<PhraseTopic> phraseTopicsList = new ArrayList<>();
        if (scrapPosts.size() == 0 && recommendPosts.size() == 0) {
            log.info("사용자가 스크랩한 게시글, 추천한 게시글이 없습니다.");
        }
        if(scrapPosts.size() != 0){
            for (ScrapDto scrapDto : scrapPosts) {
                Posts myScrapPosts = postsRepository.findById(scrapDto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + scrapDto.getPostId()));
                phraseTopicsList.add(myScrapPosts.getPhraseTopic());
            }
        }
        if(recommendPosts.size() != 0){
            for (RecommendDto recommendDto : recommendPosts) {
                Posts myRecommendPosts = postsRepository.findById(recommendDto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + recommendDto.getPostId()));
                phraseTopicsList.add(myRecommendPosts.getPhraseTopic());
            }
        }
        log.info("phraseTopicList : {}", phraseTopicsList);

        // 빈도수를 Map 에 저장
        Map<PhraseTopic, Integer> map = new HashMap<>();
        for(PhraseTopic phraseTopic: phraseTopicsList){
            Integer count = map.get(phraseTopic);
            if(count == null){
                map.put(phraseTopic, 1);
            }else{
                map.put(phraseTopic, count+1);
            }
        }

        // 최대값 찾기 위해 Comparator 정의
        Comparator<Map.Entry<PhraseTopic, Integer>> comparator = Map.Entry.comparingByValue();
        Map.Entry<PhraseTopic, Integer> maxEntry = Collections.max(map.entrySet(), comparator);
        log.info("UserId : {}의 가장 선호도가 높은 phraseTopic: {}, 빈도수 : {}", userId, maxEntry.getKey(), maxEntry.getValue());
        return maxEntry.getKey();
    }

    // PhraseTopic 선호도로 글귀 추천
    @Transactional(readOnly = true)
    public Page<PostsListResponseDto> contentsBasedRecommendations(Pageable pageable, PhraseTopic phraseTopic) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        // 일주일 전
        LocalDateTime oneDaysAgo = now.minusDays(7);
        String endDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String startDate = oneDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("{}부터 {} 사이 사용자가 선호한 데이터를 바탕으로 {} 장르 글귀 5개 추천", startDate, endDate, phraseTopic);
        Page<Posts> posts = postsRepository.findTop5ByPhraseTopicOrderByScrapCountDescRecommendCountDescIdDesc(pageable, phraseTopic, startDate, endDate);

        return posts.map(PostsListResponseDto::new);
    }
}


