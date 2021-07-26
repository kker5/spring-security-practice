package me.benny.practice.spring.security.notice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Notice saveNotice(String title, String content) {
        return noticeRepository.save(new Notice(title, content));
    }

    public void deleteNotice(Long id) {
        noticeRepository.findById(id).ifPresent(noticeRepository::delete);
    }
}
