package me.benny.practice.spring.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 회원가입 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignUpController {

    private final UserService userService;

    /**
     * @return 회원가입 페이지 리소스
     */
    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String signup(
            @ModelAttribute UserRegisterDto userDto
    ) {
        userService.signup(userDto.getUsername(), userDto.getPassword());
        return "redirect:login";
    }
}
