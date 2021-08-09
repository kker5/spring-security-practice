package me.benny.practice.spring.security.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCotroller {

    private final PostService postService;

    /**
     * 어드민인 경우 게시글 조회
     * @return admin/index.html
     */
    @GetMapping
    public String getPostForAdmin(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<Post> posts = postService.findByUser(user);
        model.addAttribute("posts", posts);
        return "admin/index";
    }
}
