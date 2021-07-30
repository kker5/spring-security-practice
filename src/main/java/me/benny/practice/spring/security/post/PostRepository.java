package me.benny.practice.spring.security.post;

import java.util.List;
import me.benny.practice.spring.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserAndStatusOrderByIdDesc(User user, PostStatus status);

    List<Post> findByStatusOrderByIdDesc(PostStatus status);

    Post findByIdAndUser(Long id, User user);
}
