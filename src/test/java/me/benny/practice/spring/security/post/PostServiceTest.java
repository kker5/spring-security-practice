package me.benny.practice.spring.security.post;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    void findByUser_유저가_게시물조회() {
        // given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        postRepository.save(new Post("title1", "content1", user));
        postRepository.save(new Post("title2", "content2", user));
        // when
        List<Post> posts = postService.findByUser(user);
        // then
        then(posts.size()).isEqualTo(2);
        Post post1 = posts.get(0);
        Post post2 = posts.get(1);

        // post1 = title2
        then(post1.getUser().getUsername()).isEqualTo("username");
        then(post1.getTitle()).isEqualTo("title2"); // 가장 늦게 insert된 데이터가 먼저 나와야합니다.
        then(post1.getContent()).isEqualTo("content2");
        // post2 = title1
        then(post2.getUser().getUsername()).isEqualTo("username");
        then(post2.getTitle()).isEqualTo("title1");
        then(post2.getContent()).isEqualTo("content1");
    }

    @Test
    void findByUser_어드민이_조회() {
        // given
        User admin = userRepository.save(new User("admin", "password", "ROLE_ADMIN"));
        User user1 = userRepository.save(new User("username", "password", "ROLE_USER"));
        User user2 = userRepository.save(new User("username2", "password", "ROLE_USER"));
        postRepository.save(new Post("title1", "content1", user1));
        postRepository.save(new Post("title2", "content2", user1));
        postRepository.save(new Post("title3", "content3", user2));
        // when
        List<Post> posts = postService.findByUser(admin);
        // then
        then(posts.size()).isEqualTo(3);
        Post post1 = posts.get(0);
        Post post2 = posts.get(1);
        Post post3 = posts.get(2);

        // post1 = title3
        then(post1.getUser().getUsername()).isEqualTo("username2");
        then(post1.getTitle()).isEqualTo("title3"); // 가장 늦게 insert된 데이터가 먼저 나와야합니다.
        then(post1.getContent()).isEqualTo("content3");
        // post2 = title2
        then(post2.getUser().getUsername()).isEqualTo("username");
        then(post2.getTitle()).isEqualTo("title2");
        then(post2.getContent()).isEqualTo("content2");
        // post3 = title1
        then(post3.getUser().getUsername()).isEqualTo("username");
        then(post3.getTitle()).isEqualTo("title1");
        then(post3.getContent()).isEqualTo("content1");
    }

    @Test
    void savePost() {
        // given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        // when
        postService.savePost(user, "title1", "content1");
        // then
        then(postRepository.count()).isOne();
    }

    @Test
    void deletePost() {
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        Post post = postRepository.save(new Post("title1", "content1", user));
        postService.deletePost(user, post.getId());
        // then
        then(postRepository.count()).isZero();
    }
}