package me.benny.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // permit
        http.authorizeRequests()
            // /와 /home은 모두에게 허용
            .antMatchers("/", "/css/**", "/home", "/example", "/signup").permitAll()
            // hello 페이지는 USER 롤을 가진 유저에게만 허용
            .antMatchers("/post").hasRole("USER")
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
            .anyRequest().authenticated();
        // login
        http.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .permitAll(); // 모두 허용
        // logout
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}
