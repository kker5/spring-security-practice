package me.benny.practice.spring.security.post;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 등록 Dto
 */
@Getter
@Setter
public class PostRegisterDto {

    private String title;
    private String content;
}