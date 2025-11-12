package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.NoticeRequest;
import kr.co.goldenhome.entity.Notice;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.implement.NotificationSender;
import kr.co.goldenhome.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.co.goldenhome.exception.ErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NotificationSender notificationSender;

    public void write(NoticeRequest request, Long userId) {
        noticeRepository.save(Notice.create(request.title(), request.content(), userId));
    }

    public Notice read(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new CustomException(NOT_FOUND, "NoticeService.read"));
    }

    public List<Notice> readAll(Long lastId, Long pageSize) {
        return lastId == null ?
                noticeRepository.findAllInfiniteScroll(pageSize) :
                noticeRepository.findAllInfiniteScroll(lastId, pageSize);
    }


}
