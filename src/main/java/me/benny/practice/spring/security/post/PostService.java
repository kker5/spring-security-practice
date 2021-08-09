package me.benny.practice.spring.security.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 게시글 조회
     * 유저는 본인의 게시글만 조회할 수 있다.
     * 어드민은 모든 게시글을 조회할 수 있다.
     * @param user 게시글을 찾을 유저
     * @return 유저가 조회할 수 있는 모든 게시물 List
     */
    @Transactional(readOnly = true)
    public List<Post> findByUser(User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.isAdmin()) {
            return postRepository.findAll(Sort.by(Direction.DESC, "id"));
        }
        return postRepository.findByUserOrderByIdDesc(user);
    }

    /**
     * 게시글 저장
     * @param user 게시글 저장하는 유저
     * @param title 제목
     * @param content 내용
     * @return 저장된 게시글
     */
    public Post savePost(User user, String title, String content) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        return postRepository.save(new Post(title, content, user));
    }

    /**
     * 게시글 삭제
     * @param user 삭제하려는 게시글의 유저
     * @param postId 게시글 ID
     */
    public void deletePost(User user, Long postId) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        Post post = postRepository.findByIdAndUser(postId, user);
        postRepository.delete(post);
    }
}
