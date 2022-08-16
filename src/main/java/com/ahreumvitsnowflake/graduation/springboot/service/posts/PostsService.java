package com.ahreumvitsnowflake.graduation.springboot.service.posts;

import com.ahreumvitsnowflake.graduation.springboot.domain.posts.PostsRepository;
import com.ahreumvitsnowflake.graduation.springboot.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

}
