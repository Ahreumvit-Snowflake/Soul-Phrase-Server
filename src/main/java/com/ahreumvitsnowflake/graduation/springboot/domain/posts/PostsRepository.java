package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    @Modifying
    @Query("UPDATE Posts SET viewCount = viewCount + 1 WHERE id = :id")
    int updateViewCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET scrapCount = scrapCount + 1 WHERE id = :id")
    int plusScrapCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET scrapCount = scrapCount - 1 WHERE id = :id")
    int minusScrapCount(@Param("id") Long id);
}
