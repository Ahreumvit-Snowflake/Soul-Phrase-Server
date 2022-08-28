package com.ahreumvitsnowflake.graduation.springboot.domain.scrap;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    // 해당 유저가 스크랩을 한 적이 있는지 체크하는 용도로 사용
    Optional<Scrap> findByUserAndPosts(User user, Posts posts);

    // 게시글의 스크랩 수 카운트
    Optional<Integer> countByPosts(Posts posts);

    // 모든 스크랩 데이터 조회
    @Query("SELECT s FROM Scrap s ORDER BY s.id DESC")
    List<Scrap> findAllDesc();
}
