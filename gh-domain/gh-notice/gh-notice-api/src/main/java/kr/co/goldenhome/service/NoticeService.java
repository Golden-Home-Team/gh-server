package kr.co.goldenhome.service;

import kr.co.goldenhome.FcmManager;
import kr.co.goldenhome.dto.NoticeRequest;
import kr.co.goldenhome.entity.Notice;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.co.goldenhome.exception.ErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FcmManager fcmManager;

    public void write(NoticeRequest request, Long userId) {
        noticeRepository.save(Notice.create(request.title(), request.content(), userId));
        fcmManager.sendMessages(request.title(), request.content(), "notice_topic"); // 이런 부분 때문에 ENUM 을 공통으로 내려주도록 하는게 나을듯
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
