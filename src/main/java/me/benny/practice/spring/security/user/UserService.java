package me.benny.practice.spring.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(
        String username,
        String password
    ) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("이미 등록된 유저입니다.");
        }
        return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_USER"));
    }

    public User signupAdmin(
        String username,
        String password
    ) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("이미 등록된 유저입니다.");
        }
        return userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_ADMIN"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}