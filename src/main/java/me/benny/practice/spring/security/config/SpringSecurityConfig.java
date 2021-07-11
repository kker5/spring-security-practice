package me.benny.practice.spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author benny.ahn
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // permit
        http.authorizeRequests()
            // /와 /home은 모두에게 허용
            .antMatchers("/", "/home","/example").permitAll()
            // hello 페이지는 USER 롤을 가진 유저에게만 허용
            .antMatchers("/private").hasRole("USER")
            .antMatchers("/admin").hasRole("ADMIN")
            .anyRequest().authenticated();
        // login
        http.formLogin() // 기본 로그인 인증 제공
            .defaultSuccessUrl("/")
            .permitAll() // 모두 허용
            .and()
            .logout();  // 기본 로그아웃 제공
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // password encorder
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // user 정보
        UserDetails user = User.withUsername("user")
            .password(encoder.encode("user"))
            .roles("USER") // user에 ROLE_USER라는 롤이 추가됩니다. "ROLE_" 은 자동으로 추가됩니다.
            .build();
        // admin 정보
        UserDetails admin = User.withUsername("admin")
            .password(encoder.encode("admin"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
