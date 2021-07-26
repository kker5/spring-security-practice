package me.benny.practice.spring.security.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String findByPost(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        List<Post> posts = postService.findByUser(user);
        model.addAttribute("posts", posts);
        return "post/index";
    }

    @PostMapping
    public String savePost(Authentication authentication, @ModelAttribute PostDto postDto) {
        User user = (User) authentication.getPrincipal();
        postService.savePost(user, postDto.getTitle(), postDto.getContent());
        return "redirect:post";
    }

    @DeleteMapping
    public String deletePost(Authentication authentication, @RequestParam Long id) {
        User user = (User) authentication.getPrincipal();
        postService.deletePost(user, id);
        return "redirect:post";
    }
}

