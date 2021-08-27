package me.benny.practice.spring.security.config;

import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.filter.StopwatchFilter;
import me.benny.practice.spring.security.filter.TesterAuthenticationFilter;
import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // stopwath filter
        http.addFilterBefore(
                new StopwatchFilter(),
                WebAsyncManagerIntegrationFilter.class
        );
        // tester authentication filter
        http.addFilterBefore(
                new TesterAuthenticationFilter(this.authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        );
        // basic authentication
        http.httpBasic().disable(); // basic authentication filter 비활성화
        // csrf
        http.csrf();
        // remember-me
        http.rememberMe();
        // authorization
        http.authorizeRequests()
                // /와 /home은 모두에게 허용
                .antMatchers("/", "/home", "/signup").permitAll()
                // hello 페이지는 USER 롤을 가진 유저에게만 허용
                .antMatchers("/note").hasRole("USER")
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

    @Override
    public void configure(WebSecurity web) {
        // 정적 리소스 spring security 대상에서 제외
//        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * UserDetailsService 구현
     *
     * @return UserDetailsService
     */
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
