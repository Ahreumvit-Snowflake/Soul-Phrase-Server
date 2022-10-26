package com.ahreumvitsnowflake.graduation.springboot.domain.posts;

import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    // 전체 게시글 조회
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();

    // 전체 글 페이징 처리해서 조회
    @Query("SELECT p FROM Posts p")
    Page<PostsListResponseDto> findPageAllDesc(Pageable pageable);

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
    Page<PostsListResponseDto> findByCondition(Pageable pageable, @Param("category") Category category);

    @Query("SELECT p FROM Posts p WHERE phraseTopic = :phraseTopic")
    Page<PostsListResponseDto> findByPhraseTopic(Pageable pageable, @Param("phraseTopic") PhraseTopic phraseTopic);

    @Query("SELECT p FROM Posts p WHERE category = :category AND phraseTopic = :phraseTopic")
    Page<PostsListResponseDto> findByConditionAndPhraseTopic(Pageable pageable, @Param("category") Category category, @Param("phraseTopic") PhraseTopic phraseTopic);

    @Modifying
    @Query("UPDATE Posts SET recommendCount = recommendCount + 1 WHERE id = :id")
    int plusRecommendCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET recommendCount = recommendCount - 1 WHERE id = :id")
    int minusRecommendCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET dislikeCount = dislikeCount + 1 WHERE id = :id")
    int plusDislikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET dislikeCount = dislikeCount - 1 WHERE id = :id")
    int minusDislikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Posts SET reportCount = reportCount + 1 WHERE id = :id")
    int plusReportCount(@Param("id") Long id);

    // 신고수가 특정 개수 이상인 게시물 리스트 조회
    @Query("SELECT p FROM Posts p WHERE reportCount = :reportCount")
    List<PostsListResponseDto> findByReportCount(@Param("reportCount") int reportCount);

    // 일주일 간 인기글 Top 5 조회
    @Query("SELECT p FROM Posts p WHERE createdDate >= :startDate AND createdDate <= :endDate ORDER BY p.scrapCount DESC, p.recommendCount DESC, p.id DESC")
    Slice<Posts> findTop10ByOrderByScrapCountDescRecommendCountDescIdDesc(Pageable pageable, @Param("startDate") String startDate, @Param("endDate") String endDate);

    // 글귀 추천
    @Query("SELECT p FROM Posts p WHERE phraseTopic = :phraseTopic AND createdDate >= :startDate AND createdDate <= :endDate ORDER BY p.scrapCount DESC, p.recommendCount DESC, p.id DESC")
    Page<Posts> findTop5ByPhraseTopicOrderByScrapCountDescRecommendCountDescIdDesc(Pageable pageable, @Param("phraseTopic") PhraseTopic phraseTopic, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
