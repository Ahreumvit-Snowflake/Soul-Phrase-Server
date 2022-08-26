package com.ahreumvitsnowflake.graduation.springboot.config.auth;

import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.OAuthAttributes;
import com.ahreumvitsnowflake.graduation.springboot.config.auth.dto.SessionUser;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.User;
import com.ahreumvitsnowflake.graduation.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Security UserDetailsService == OAuth OAuth2UserService
 * */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 OAuth2 서비스 구분 코드(구글, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // OAuth2 로그인 진행 시 키가 되는 필드값(PK) (구글의 기본 코드는 "sub")
        // 네이버, 구글 로그인 동시 지원할 때 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                            .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // 세션 정보를 저장하는 직렬화된 Dto 클래스
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 소셜로그인시 기존 회원이 존재하면 수정날짜 정보만 업데이트해 기존의 데이터는 그대로 보존
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.snsUpdate(attributes.getUsername(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }

    public SessionUser findById(Long userId) {
        User entity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));
        log.info("CustomOAuthUserService findById() 실행");
        return new SessionUser(entity);
    }

    @Transactional
    public Long updateNickname(Long userId, SessionUser requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));

        user.updateNickname(requestDto.getNickname());
        log.info("CustomOAuthUserService updateNickname() 실행");
        return userId;
    }

    @Transactional
    public void delete(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));
        userRepository.delete(user);
        log.info("CustomOAuthUserService delete() 실행");
    }

    @Transactional(readOnly = true)
    public List<SessionUser> findAllDesc() {
        return userRepository.findAllDesc().stream()
                .map(SessionUser::new)
                .collect(Collectors.toList());
    }
}
