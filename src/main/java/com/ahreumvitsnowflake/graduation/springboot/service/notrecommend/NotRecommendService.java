package com.ahreumvitsnowflake.graduation.springboot.service.notrecommend;

import com.ahreumvitsnowflake.graduation.springboot.domain.notrecommend.NotRecommend;
import com.ahreumvitsnowflake.graduation.springboot.domain.notrecommend.NotRecommendRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.NotRecommendDto;
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
public class NotRecommendService {
    private final NotRecommendRepository notRecommendRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    // 비추천 등록/취소 한 번에!
    @Transactional
    public int updateNotRecommend(Long userId, Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        // 중복 추천 방지
        if(!isNotAlreadyNotRecommend(user, posts)){
            notRecommendRepository.save(new NotRecommend(posts, user));
            postsRepository.plusRecommendCount(postId);
            return 1;
        } else {
            NotRecommend notRecommend = notRecommendRepository.findByUserAndPosts(user, posts)
                    .orElseThrow(() -> new IllegalArgumentException("해당 추천이 없습니다. id="));
            notRecommendRepository.delete(notRecommend);
            postsRepository.minusRecommendCount(postId);
            return 2;
        }
    }

    //  비추천 등록
    @Transactional
    public boolean addNotRecommend(Long userId, Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        // 중복 비추천 방지
        if(!isNotAlreadyNotRecommend(user, posts)){
            notRecommendRepository.save(new NotRecommend(posts, user));
            postsRepository.plusNotRecommendCount(postId);
            return true;
        }
        return false;
    }

    // 비추천 취소
    @Transactional
    public void cancelNotRecommend(Long userId, Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        NotRecommend notRecommend = notRecommendRepository.findByUserAndPosts(user, posts)
                .orElseThrow(() -> new IllegalArgumentException("해당 비추천이 없습니다. id="));
        notRecommendRepository.delete(notRecommend);
        postsRepository.minusNotRecommendCount(postId);
    }

    // 사용자가 이미 비추천한 게시글인지 체크
    @Transactional
    private boolean isNotAlreadyNotRecommend(User user, Posts posts) {
        return notRecommendRepository.findByUserAndPosts(user, posts).isPresent();
    }

    // 해당 게시물을 비추천한 사용자 리스트 반환
    public List<String> thisPostsNotRecommendUserList(Long postId, Long userId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        Integer postsNotRecommendCount = notRecommendRepository.countByPosts(posts).orElse(0);
        List<String> resultData = new ArrayList<>(Arrays.asList(String.valueOf(postsNotRecommendCount)));

        if(Objects.nonNull(user)){
            resultData.add(String.valueOf(isNotAlreadyNotRecommend(user, posts)));
            return resultData;
        }
        return resultData;
    }

    // 내가 비추천한 글 모두 조회
    @Transactional
    public List<NotRecommendDto> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 아이디가 없습니다. id="+ userId));
        return notRecommendRepository.findByUser(user);
    }
}
