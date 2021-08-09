package me.benny.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.notice.NoticeService;
import me.benny.practice.spring.security.post.PostService;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 초기 상태 등록 Config
 */
@Configuration
@RequiredArgsConstructor
@Profile(value = "!test")
public class InitializeConfig {

    private final UserService userService;
    private final PostService postService;
    private final NoticeService noticeService;

    /**
     * <h2>유저 등록</h2>
     * 1. user / user<br/> 2. admin / admin
     * <h2>게시글 등록 4개</h2>
     * <h2>공지사항 등록 2개</h2>
     */
    @Bean
    public void adminAccount() {
        User user = userService.signup("user", "user");
        userService.signupAdmin("admin", "admin");
        postService.savePost(user, "테스트", "테스트입니다.");
        postService.savePost(user, "테스트2", "테스트2입니다.");
        postService.savePost(user, "테스트3", "테스트3입니다.");
        postService.savePost(user, "여름 여행계획", "여름 여행계획 작성중...");
        noticeService.saveNotice("환영합니다.", "환영합니다 여러분");
        noticeService.saveNotice("게시글 작성 방법 공지", "1. 회원가입\n2. 로그인\n3. 게시글 작성\n4. 저장\n* 본인 외에는 게시글을 볼 수 없습니다.");
    }
}
