package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    // 스크랩 많은 순서로 정렬
    @Query("SELECT p FROM Posts p ORDER BY p.scrapCount DESC, p.id DESC ")
    List<Posts> findOrderByScrapCountDescIdDesc();

    @Modifying
    @Query("UPDATE Posts SET viewCount = viewCount + 1 WHERE id = :id")
    int updateViewCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET scrapCount = scrapCount + 1 WHERE id = :id")
    int plusScrapCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET scrapCount = scrapCount - 1 WHERE id = :id")
    int minusScrapCount(@Param("id") Long id);

    @Query("SELECT p FROM Posts p WHERE user = :user")
    List<PostsListResponseDto> findByUser(@Param("user") User user);

    @Query("SELECT p FROM Posts p WHERE category = :category")
    List<PostsListResponseDto> findByCondition(@Param("category") Category category);

    @Query("SELECT p FROM Posts p WHERE phraseTopic = :phraseTopic")
    List<PostsListResponseDto> findByPhraseTopic(@Param("phraseTopic") PhraseTopic phraseTopic);

    @Query("SELECT p FROM Posts p WHERE category = :category AND phraseTopic = :phraseTopic")
    List<PostsListResponseDto> findByConditionAndPhraseTopic(@Param("category") Category category, @Param("phraseTopic") PhraseTopic phraseTopic);

    @Query("SELECT p FROM Posts p WHERE category = :category ORDER BY p.scrapCount DESC, p.id DESC")
    List<PostsListResponseDto> findByConditionOrderByScrapCountDescIdDesc(@Param("category") Category category);

    @Query("SELECT p FROM Posts p WHERE phraseTopic = :phraseTopic ORDER BY p.scrapCount DESC, p.id DESC")
    List<PostsListResponseDto> findByPhraseTopicOrderByScrapCountDescIdDesc(@Param("phraseTopic") PhraseTopic phraseTopic);

    @Query("SELECT p FROM Posts p WHERE category = :category AND phraseTopic = :phraseTopic ORDER BY p.scrapCount DESC, p.id DESC")
    List<PostsListResponseDto> findByConditionAndPhraseTopicOrderByScrapCountDescIdDesc(@Param("category") Category category, @Param("phraseTopic") PhraseTopic phraseTopic);

    Slice<Posts> findSliceBy(Pageable pageable);

    @Modifying
    @Query("UPDATE Posts SET recommendCount = recommendCount + 1 WHERE id = :id")
    int plusRecommendCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET recommendCount = recommendCount - 1 WHERE id = :id")
    int minusRecommendCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET notRecommendCount = notRecommendCount + 1 WHERE id = :id")
    int plusNotRecommendCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET notRecommendCount = notRecommendCount - 1 WHERE id = :id")
    int minusNotRecommendCount(@Param("id") Long id);
}
