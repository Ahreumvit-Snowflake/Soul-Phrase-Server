package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
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

//    @Modifying
//    @Query("SELECT p FROM Posts p WHERE user := :user")
//    List<Posts> findByUser(@Param("user") SessionUser user);

    @Query("SELECT p FROM Posts p WHERE category = :category")
    List<PostsListResponseDto> findByCondition(@Param("category") Category category);

    @Query("SELECT p FROM Posts p WHERE phraseTopic = :phraseTopic")
    List<PostsListResponseDto> findByPhraseTopic(@Param("phraseTopic") PhraseTopic phraseTopic);

    @Query("SELECT p FROM Posts p WHERE category = :category AND phraseTopic = :phraseTopic")
    List<PostsListResponseDto> findByConditionAndPhraseTopic(@Param("category") Category category, @Param("phraseTopic") PhraseTopic phraseTopic);
}
