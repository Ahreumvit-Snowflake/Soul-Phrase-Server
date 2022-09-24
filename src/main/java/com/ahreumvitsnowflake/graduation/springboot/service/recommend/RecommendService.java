package com.ahreumvitsnowflake.graduation.springboot.service.recommend;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.Recommend;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.RecommendRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    // 추천 등록/취소 한 번에!
    @Transactional
    public int updateRecommend(Long userId, Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        // 중복 추천 방지
        if(!isNotAlreadyRecommend(user, posts)){
            recommendRepository.save(new Recommend(posts, user));
            postsRepository.plusRecommendCount(postId);
            return 1;
        } else {
            Recommend recommend = recommendRepository.findByUserAndPosts(user, posts)
                    .orElseThrow(() -> new IllegalArgumentException("해당 추천이 없습니다. id="));
            recommendRepository.delete(recommend);
            postsRepository.minusRecommendCount(postId);
            return 2;
        }
    }

    // 추천 등록
    @Transactional
    public boolean addRecommend(Long userId, Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        // 중복 스크랩 방지
        if(!isNotAlreadyRecommend(user, posts)){
            recommendRepository.save(new Recommend(posts, user));
            postsRepository.plusRecommendCount(postId);
            return true;
        }
        return false;
    }

    // 추천 취소
    @Transactional
    public void cancelRecommend(Long userId, Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        Recommend recommend = recommendRepository.findByUserAndPosts(user, posts)
                .orElseThrow(() -> new IllegalArgumentException("해당 추천이 없습니다. id="));
        recommendRepository.delete(recommend);
        postsRepository.minusRecommendCount(postId);
    }

    // 사용자가 이미 추천한 게시글인지 체크
    @Transactional
    private boolean isNotAlreadyRecommend(User user, Posts posts) {
        return recommendRepository.findByUserAndPosts(user, posts).isPresent();
    }

    // 해당 게시물을 추천한 사용자 리스트 반환
    public List<String> thisPostsRecommendUserList(Long postId, Long userId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        Integer postsRecommendCount = recommendRepository.countByPosts(posts).orElse(0);
        List<String> resultData = new ArrayList<>(Arrays.asList(String.valueOf(postsRecommendCount)));

        if(Objects.nonNull(user)){
            resultData.add(String.valueOf(isNotAlreadyRecommend(user, posts)));
            return resultData;
        }
        return resultData;
    }

    // 내가 추천한 글 모두 조회
    @Transactional
    public List<RecommendDto> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 아이디가 없습니다. id="+ userId));
        return recommendRepository.findByUser(user);
    }
}
