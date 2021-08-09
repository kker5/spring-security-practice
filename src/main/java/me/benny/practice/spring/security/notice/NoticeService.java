package me.benny.practice.spring.security.notice;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /**
     * 모든 공지사항 조회
     * @return 모든 공지사항 List
     */
    @Transactional(readOnly = true)
    public List<Notice> findAll() {
        return noticeRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    /**
     * 공지사항 저장
     * @param title 제목
     * @param content 내용
     * @return 저장된 공지사항
     */
    public Notice saveNotice(String title, String content) {
        return noticeRepository.save(new Notice(title, content));
    }

    /**
     * 공지사항 삭제
     * @param id ID
     */
    public void deleteNotice(Long id) {
        noticeRepository.findById(id).ifPresent(noticeRepository::delete);
    }
}
