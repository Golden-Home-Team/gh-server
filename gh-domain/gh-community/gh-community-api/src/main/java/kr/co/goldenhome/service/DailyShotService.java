package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.DailyShot;
import kr.co.goldenhome.implement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyShotService {

    private final DailyShotAppender dailyShotAppender;
    private final DailyShotUpdater dailyShotUpdater;
    private final DailyShotReader dailyShotReader;
    private final DailyShotImageAppender dailyShotImageAppender;
    private final DailyShotImageUpdater dailyShotImageUpdater;
    private final DailyShotImageReader dailyShotImageReader;

    @Transactional
    public void write(DailyShotRequest request, Long facilityId) {
        Long dailyShotId = dailyShotAppender.save(facilityId, request.content(), request.recordDate());
        dailyShotImageAppender.saveAll(dailyShotId, request.dailyShotImageInfoRequests());
    }

    @Transactional
    public void update(DailyShotUpdateRequest request, Long dailyShotId) {
        dailyShotUpdater.update(dailyShotId, request.content());
        dailyShotImageUpdater.update(request.dailyShotImageInfoRequests(), dailyShotId);
    }

    public DailyShotMainResponse readOnMain(Long facilityId) {
        DailyShot dailyShot = dailyShotReader.getLatestByFacilityId(facilityId);
        Long dailyShotId = Optional.ofNullable(dailyShot).map(DailyShot::getId).orElse(null);
        String content = Optional.ofNullable(dailyShot).map(DailyShot::getContent).orElse(null);
        DailyShotImageResponse dailyShotImageResponse = dailyShotImageReader.getLatestByDailyShotId(dailyShotId);
        return new DailyShotMainResponse(dailyShotId, content, dailyShotImageResponse.id(), dailyShotImageResponse.imageUrl());
    }

    public DailyShotResponse readByDayOfWeek(Long facilityId, DayOfWeek dayOfWeek) {
        DailyShot dailyShot = dailyShotReader.getLatestByFacilityIdAndDayOfWeek(facilityId, dayOfWeek.getValue());
        Long dailyShotId = Optional.ofNullable(dailyShot).map(DailyShot::getId).orElse(null);
        String content = Optional.ofNullable(dailyShot).map(DailyShot::getContent).orElse(null);
        LocalDate recordDate = Optional.ofNullable(dailyShot).map(DailyShot::getRecordDate).orElse(null);
        List<DailyShotImageResponse> dailyShotImageResponses = dailyShotImageReader.getImagesByDailyShotId(dailyShotId);
        return new DailyShotResponse(dailyShotId, content, recordDate, dailyShotImageResponses);
    }
}
