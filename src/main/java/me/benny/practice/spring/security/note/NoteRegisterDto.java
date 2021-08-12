package me.benny.practice.spring.security.note;

import lombok.Getter;
import lombok.Setter;

/**
 * 노트 등록 Dto
 */
@Getter
@Setter
public class NoteRegisterDto {

    private String title;
    private String content;
}