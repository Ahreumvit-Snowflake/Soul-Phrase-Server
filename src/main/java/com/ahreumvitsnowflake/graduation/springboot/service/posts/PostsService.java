package com.ahreumvitsnowflake.graduation.springboot.service.posts;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Category;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PhraseTopic;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.Posts;
import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsListResponseDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsResponseDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    public Long save(PostsSaveRequestDto requestDto, String email){
        // User 정보를 가져와 dto에 담아준다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일이 없습니다. email="+ email));

        // requestDto.setUser(user);와 같은 효과
        requestDto = PostsSaveRequestDto.builder()
                .category(requestDto.getCategory())
                .phraseTopic(requestDto.getPhraseTopic())
                .writer(requestDto.getWriter())
                .phrase(requestDto.getPhrase())
                .scrapCount(requestDto.getScrapCount())
                .source(requestDto.getSource())
                .user(user)
                .build();

        log.info("PostsService save() 실행");
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id="+ postId));
        posts.update(requestDto.getCategory(), requestDto.getPhraseTopic(), requestDto.getWriter(),
                requestDto.getPhrase(), requestDto.getScrapCount(), requestDto.getSource(), requestDto.getRecommendCount(), requestDto.getDislikeCount(), requestDto.getReportCount());
        return postId;
    }

    @Transactional
    public void delete(Long postId){
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + postId));
        postsRepository.delete(posts);
    }

    public PostsResponseDto findById(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + postId));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    // View Counting
    @Transactional
    public int updateViewCount(Long id){
        return postsRepository.updateViewCount(id);
    }

    // plus Scrap Counting
    @Transactional
    public int plusScrapCount(Long id){
        return postsRepository.plusScrapCount(id);
    }

    // minus Scrap Counting
    @Transactional
    public int minusScrapCount(Long id){
        return postsRepository.minusScrapCount(id);
    }

    // plus Recommend Counting
    @Transactional
    public void plusRecommendCount(Long id){
        postsRepository.plusRecommendCount(id);
    }

    // minus Recommend Counting
    @Transactional
    public void minusRecommendCount(Long id){
        postsRepository.minusRecommendCount(id);
    }

    // plus Report Counting
    @Transactional
    public void plusReportCount(Long id){
        postsRepository.plusReportCount(id);
    }

    // 내가 쓴 글 조회
    @Transactional
    public List<PostsListResponseDto> findByUser(Long userId) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 아이디가 없습니다. id="+userId));
        return postsRepository.findByUser(writer);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByCondition(Category category) {
        return postsRepository.findByCondition(category);
    }

    // 최신순 정렬
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByConditions(Category category, PhraseTopic phraseTopic) {
        if (null == category && null == phraseTopic) {
            return postsRepository.findAllDesc().stream()
                    .map(PostsListResponseDto::new)
                    .collect(Collectors.toList());
        } else if (null == category) {
            return postsRepository.findByPhraseTopic(phraseTopic);
        } else if (null == phraseTopic) {
            return postsRepository.findByCondition(category);
        } else return postsRepository.findByConditionAndPhraseTopic(category, phraseTopic);
    }

    // 스크랩순 정렬
    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findByConditionsOrderByScrapCount(Category category, PhraseTopic phraseTopic) {
        if (null == category && null == phraseTopic) {
            return postsRepository.findOrderByScrapCountDescIdDesc().stream()
                    .map(PostsListResponseDto::new)
                    .collect(Collectors.toList());
        } else if (null == category) {
            return postsRepository.findByPhraseTopicOrderByScrapCountDescIdDesc(phraseTopic);
        } else if (null == phraseTopic) {
            return postsRepository.findByConditionOrderByScrapCountDescIdDesc(category);
        } else return postsRepository.findByConditionAndPhraseTopicOrderByScrapCountDescIdDesc(category, phraseTopic);
    }

    // Paging
    @Transactional(readOnly = true)
    public Slice<PostsListResponseDto> pageList(Pageable pageable){
        Slice<Posts> postsSlice = postsRepository.findSliceBy(pageable);
        return postsSlice.map(PostsListResponseDto::new);
    }

    // 1시간마다 신고수가 2개 이상인 posts 조회해서 삭제
    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional
    public void deletePostsByReports(){
        List<PostsListResponseDto> postsList = postsRepository.findByReportCount(2);
        if(postsList.size() == 0){
            log.info("신고수 2 이상인 게시물이 없습니다.");
        }
        else{
            for (PostsListResponseDto postsListResponseDto : postsList) {
                Posts posts = postsRepository.findById(postsListResponseDto.getPostId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id=" + postsListResponseDto.getPostId()));
                delete(posts.getId());
                log.info("신고수 2 이상이라, postId : {}인 글이 자동 삭제되었습니다", posts.getId());
            }
        }
    }

    // 일주일 간 인기글 Top 5 조회
    @Transactional
    public Slice<PostsListResponseDto> popularPosts(Pageable pageable) {
        // 현재 시간
        LocalDateTime now = LocalDateTime.now();
        // 일주일 전
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        // test : 현재 시간에서 5분 전
        // LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

        String endDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String startDate = sevenDaysAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //String startDate = fiveMinutesAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Slice<Posts> postsSlice = postsRepository.findTop5ByOrderByScrapCountDescRecommendCountDescIdDesc(pageable, startDate, endDate);
        log.info("{}부터 {} 사이 인기글 Top 5 조회", startDate, endDate);
        return postsSlice.map(PostsListResponseDto::new);
    }

//    // 스크랩 많은 순서로 정렬
//    @Transactional(readOnly = true)
//    public List<PostsListResponseDto> findOrderByScrapCountDescIdDesc() {
//        return postsRepository.findOrderByScrapCountDescIdDesc().stream()
//                .map(PostsListResponseDto::new)
//                .collect(Collectors.toList());
//    }

}
