package com.ahreumvitsnowflake.graduation.springboot.service.posts;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                requestDto.getPhrase(), requestDto.getScrapCount(), requestDto.getSource());
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
}
