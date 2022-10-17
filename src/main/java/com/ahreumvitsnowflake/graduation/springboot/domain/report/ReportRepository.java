package com.ahreumvitsnowflake.graduation.springboot.domain.report;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // 해당 유저가 신고를 한 적이 있는지 체크하는 용도로 사용
    Optional<Report> findByUserAndPosts(User user, Posts posts);

    // 게시글의 신고수 카운트
    int countByPosts(Posts posts);
}
