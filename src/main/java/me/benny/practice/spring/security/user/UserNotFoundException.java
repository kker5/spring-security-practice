package me.benny.practice.spring.security.user;

/**
 * 유저를 찾을 수 없을 때 발생하는 Exception
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("유저를 찾을 수 없습니다.");
    }
}
