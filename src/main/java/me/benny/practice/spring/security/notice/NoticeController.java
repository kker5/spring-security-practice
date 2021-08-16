package me.benny.practice.spring.security.notice;

import lombok.RequiredArgsConstructor;
import me.benny.practice.spring.security.note.NoteRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 공지사항 서비스 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 조회
     *
     * @return notice/index.html
     */
    @GetMapping
    public String getNotice(Model model) {
        List<Notice> notices = noticeService.findAll();
        model.addAttribute("notices", notices);
        return "notice/index";
    }

    /**
     * 공지사항 등록
     *
     * @param noteDto 노트 등록 Dto
     * @return notice/index.html refresh
     */
    @PostMapping
    public String postNotice(@ModelAttribute NoteRegisterDto noteDto) {
        noticeService.saveNotice(noteDto.getTitle(), noteDto.getContent());
        return "redirect:notice";
    }

    /**
     * 공지사항 삭제
     *
     * @param id 공지사항 ID
     * @return notice/index.html refresh
     */
    @DeleteMapping
    public String deleteNotice(@RequestParam Long id) {
        noticeService.deleteNotice(id);
        return "redirect:notice";
    }
}
