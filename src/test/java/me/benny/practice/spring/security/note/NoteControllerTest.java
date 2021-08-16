package me.benny.practice.spring.security.note;

import me.benny.practice.spring.security.user.User;
import me.benny.practice.spring.security.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Transactional
class NoteControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    private MockMvc mockMvc;
    private User user;
    private User admin;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext applicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
        user = userRepository.save(new User("user", "user", "ROLE_USER"));
        admin = userRepository.save(new User("admin", "admin", "ROLE_ADMIN"));
    }

    @Test
    void getNote_인증없음() throws Exception {
        mockMvc.perform(get("/note"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getNote_인증있음() throws Exception {
        mockMvc.perform(
                get("/note").with(user(user))
        ).andExpect(status().isOk());
    }

    @Test
    void postNote_인증없음() throws Exception {
        mockMvc.perform(
                post("/note").with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void postNote_어드민인증있음() throws Exception {
        mockMvc.perform(
                post("/note").with(csrf()).with(user(admin))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void postNote_유저인증있음() throws Exception {
        mockMvc.perform(
                post("/note").with(csrf()).with(user(user))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "제목")
                        .param("content", "내용")
        ).andExpect(redirectedUrl("note")).andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteNote_인증없음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                delete("/note?id=" + note.getId()).with(csrf())
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteNote_유저인증있음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                delete("/note?id=" + note.getId()).with(csrf()).with(user(user))
        ).andExpect(redirectedUrl("note")).andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteNote_어드민인증있음() throws Exception {
        Note note = noteRepository.save(new Note("제목", "내용", user));
        mockMvc.perform(
                delete("/note?id=" + note.getId()).with(csrf()).with(user(admin))
        ).andExpect(status().is4xxClientError());
    }
}