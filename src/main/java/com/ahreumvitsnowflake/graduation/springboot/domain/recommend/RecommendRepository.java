package com.ahreumvitsnowflake.graduation.springboot.domain.recommend;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.RecommendDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    // 해당 유저가 추천을 한 적이 있는지 체크하는 용도로 사용
    Optional<Recommend> findByUserAndPosts(User user, Posts posts);

    // 게시글의 추천수 카운트
    Optional<Integer> countByPosts(Posts posts);

    // 내가 추천한 글 모두 조회
    @Query("SELECT r FROM Recommend r WHERE user = :user ORDER BY r.id DESC")
    List<RecommendDto> findByUser(@Param("user") User user);
}
