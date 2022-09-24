package com.ahreumvitsnowflake.graduation.springboot.service.recommend;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.Recommend;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.RecommendRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.recommend.RecommendType;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public int updateRecommend(Long userId, Long postId, RecommendType type) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));

        // 중복 추천 방지
        Recommend findRecommend = recommendRepository.findReRecommendByUserAndPosts(user, posts);

        if (null != findRecommend && !findRecommend.getType().equals(type))
            return 3;

        if (!isNotAlreadyRecommendType(user, posts, type)) {
            recommendRepository.save(new Recommend(posts, user, type));
            if (type.equals(RecommendType.LIKE))
                postsRepository.plusRecommendCount(postId);
            else
                postsRepository.plusDislikeCount(postId);
            return 1;
        }
        else {
            Recommend recommend = recommendRepository.findByUserAndPosts(user, posts)
                    .orElseThrow(() -> new IllegalArgumentException("해당 추천이 없습니다. id="));
            if (recommend.getType().equals(RecommendType.LIKE))
                postsRepository.minusRecommendCount(postId);
            else
                postsRepository.minusDislikeCount(postId);
            recommendRepository.delete(recommend);
            return 2;
        }
    }

    // 사용자가 이미 추천 또는 비추천한 게시글인지 체크
    @Transactional
    boolean isNotAlreadyRecommendType(User user, Posts posts, RecommendType type) {
        return recommendRepository.findByUserAndPostsAndType(user, posts, type).isPresent();
    }

    // 해당 게시물을 추천한 사용자 리스트 반환
    public List<String> thisPostsRecommendUserList(Long postId, Long userId, RecommendType type){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        Integer postsRecommendCount = recommendRepository.countByPosts(posts).orElse(0);
        List<String> resultData = new ArrayList<>(Arrays.asList(String.valueOf(postsRecommendCount)));

        if(Objects.nonNull(user)){
            resultData.add(String.valueOf(isNotAlreadyRecommendType(user, posts, type)));
            return resultData;
        }
        return resultData;
    }

    // 내가 추천한 글 모두 조회
    @Transactional
    public List<RecommendDto> myRecommendPostsList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 아이디가 없습니다. id="+ userId));
        return recommendRepository.findByUserAndRecommendPosts(user, RecommendType.LIKE);
    }

}
