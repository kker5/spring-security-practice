package me.benny.practice.spring.security.note;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.benny.practice.spring.security.user.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용
     */
    @Lob
    private String content;

    /**
     * User 참조
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Note(
            String title,
            String content,
            User user
    ) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void updateContent(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}