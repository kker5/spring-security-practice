package me.benny.practice.spring.security.user;

import lombok.*;

/**
 * 유저 회원가입용 Dto
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserRegisterDto {

    private String username;
    private String password;
}
