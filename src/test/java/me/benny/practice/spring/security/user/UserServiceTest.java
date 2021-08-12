package me.benny.practice.spring.security.user;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void signup() {
        //given
        String username = "user123";
        String password = "password";
        //when
        User user = userService.signup(username, password);
        //then
        then(user.getId()).isNotNull();
        then(user.getUsername()).isEqualTo("user123");
        then(user.getPassword()).startsWith("{bcrypt}");
        then(user.getAuthorities()).hasSize(1);
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_USER");
        then(user.isAdmin()).isFalse();
        then(user.isAccountNonExpired()).isTrue();
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue();
        then(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void signupAdmin() {
        //given
        String username = "admin123";
        String password = "password";
        //when
        User user = userService.signupAdmin(username, password);
        //then
        then(user.getId()).isNotNull();
        then(user.getUsername()).isEqualTo("admin123");
        then(user.getPassword()).startsWith("{bcrypt}");
        then(user.getAuthorities()).hasSize(1);
        then(user.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_ADMIN");
        then(user.isAdmin()).isTrue();
        then(user.isAccountNonExpired()).isTrue();
        then(user.isAccountNonLocked()).isTrue();
        then(user.isEnabled()).isTrue();
        then(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void findByUsername() {
        //given
        userRepository.save(new User("user123", "password", "ROLE_USER"));
        //when
        User user = userService.findByUsername("user123");
        //then
        then(user.getId()).isNotNull();
    }
}