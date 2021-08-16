package me.benny.practice.spring.security.note;

import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class NoteServiceTest {

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @Test
    void findByUser_유저가_게시물조회() {
        // given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        noteRepository.save(new Note("title1", "content1", user));
        noteRepository.save(new Note("title2", "content2", user));
        // when
        List<Note> notes = noteService.findByUser(user);
        // then
        then(notes.size()).isEqualTo(2);
        Note note1 = notes.get(0);
        Note note2 = notes.get(1);

        // note1 = title2
        then(note1.getUser().getUsername()).isEqualTo("username");
        then(note1.getTitle()).isEqualTo("title2"); // 가장 늦게 insert된 데이터가 먼저 나와야합니다.
        then(note1.getContent()).isEqualTo("content2");
        // note2 = title1
        then(note2.getUser().getUsername()).isEqualTo("username");
        then(note2.getTitle()).isEqualTo("title1");
        then(note2.getContent()).isEqualTo("content1");
    }

    @Test
    void findByUser_어드민이_조회() {
        // given
        User admin = userRepository.save(new User("admin", "password", "ROLE_ADMIN"));
        User user1 = userRepository.save(new User("username", "password", "ROLE_USER"));
        User user2 = userRepository.save(new User("username2", "password", "ROLE_USER"));
        noteRepository.save(new Note("title1", "content1", user1));
        noteRepository.save(new Note("title2", "content2", user1));
        noteRepository.save(new Note("title3", "content3", user2));
        // when
        List<Note> notes = noteService.findByUser(admin);
        // then
        then(notes.size()).isEqualTo(3);
        Note note1 = notes.get(0);
        Note note2 = notes.get(1);
        Note note3 = notes.get(2);

        // note1 = title3
        then(note1.getUser().getUsername()).isEqualTo("username2");
        then(note1.getTitle()).isEqualTo("title3"); // 가장 늦게 insert된 데이터가 먼저 나와야합니다.
        then(note1.getContent()).isEqualTo("content3");
        // note2 = title2
        then(note2.getUser().getUsername()).isEqualTo("username");
        then(note2.getTitle()).isEqualTo("title2");
        then(note2.getContent()).isEqualTo("content2");
        // note3 = title1
        then(note3.getUser().getUsername()).isEqualTo("username");
        then(note3.getTitle()).isEqualTo("title1");
        then(note3.getContent()).isEqualTo("content1");
    }

    @Test
    void saveNote() {
        // given
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        // when
        noteService.saveNote(user, "title1", "content1");
        // then
        then(noteRepository.count()).isOne();
    }

    @Test
    void deleteNote() {
        User user = userRepository.save(new User("username", "password", "ROLE_USER"));
        Note note = noteRepository.save(new Note("title1", "content1", user));
        noteService.deleteNote(user, note.getId());
        // then
        then(noteRepository.count()).isZero();
    }
}