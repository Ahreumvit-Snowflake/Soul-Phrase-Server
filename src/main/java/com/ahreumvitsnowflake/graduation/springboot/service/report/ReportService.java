package com.ahreumvitsnowflake.graduation.springboot.service.report;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.report.Report;
import com.ahreumvitsnowflake.graduation.springboot.domain.report.ReportRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.report.ReportType;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    // 신고 등록
    @Transactional
    public boolean addReport(Long userId, Long postId, ReportType type){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ userId));
        // 중복 신고 방지
        if(!isNotAlreadyReport(user, posts)){
            reportRepository.save(new Report(posts, user, type));
            postsRepository.plusReportCount(postId);
            return true;
        }
        return false;
    }

    // 사용자가 이미 신고한 게시글인지 체크
    @Transactional
    private boolean isNotAlreadyReport(User user, Posts posts) {
        return reportRepository.findByUserAndPosts(user, posts).isPresent();
    }

    // 게시물의 신고수 카운트
    @Transactional
    public int count(Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ postId));
        return reportRepository.countByPosts(posts);
    }
}
