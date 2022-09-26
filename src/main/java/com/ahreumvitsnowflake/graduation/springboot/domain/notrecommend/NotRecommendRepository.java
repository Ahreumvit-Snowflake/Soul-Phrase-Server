package com.ahreumvitsnowflake.graduation.springboot.domain.notrecommend;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.NotRecommendDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotRecommendRepository extends JpaRepository<NotRecommend, Long> {
    // 해당 유저가 비추천을 한 적이 있는지 체크하는 용도로 사용
    Optional<NotRecommend> findByUserAndPosts(User user, Posts posts);

    // 게시글의 비추천수 카운트
    Optional<Integer> countByPosts(Posts posts);

    // 내가 비추천한 글 모두 조회
    @Query("SELECT r FROM NotRecommend r WHERE user = :user ORDER BY r.id DESC")
    List<NotRecommendDto> findByUser(@Param("user") User user);
}
