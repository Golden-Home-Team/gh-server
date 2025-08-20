package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.*;

import kr.co.goldenhome.entity.DailyDiet;
import kr.co.goldenhome.implement.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyDietService {

    private final DailyDietAppender dailyDietAppender;
    private final DailyDietUpdater dailyDietUpdater;
    private final DailyDietReader dailyDietReader;
    private final DailyDietImageAppender dailyDietImageAppender;
    private final DailyDietImageUpdater dailyDietImageUpdater;
    private final DailyDietImageReader dailyDietImageReader;

    @Transactional
    public void write(DailyDietRequest request, Long facilityId) {
        Long dailyDietId = dailyDietAppender.save(facilityId, request.content(), request.recordDate());
        dailyDietImageAppender.saveAll(dailyDietId, request.dailyDietImageInfoRequests());
    }

    @Transactional
    public void update(DailyDietUpdateRequest request, Long dailyDietId) {
        dailyDietUpdater.update(dailyDietId, request.content());
        dailyDietImageUpdater.update(request.dailyDietImageInfoRequests(), dailyDietId);
    }

    public DailyDietMainResponse readOnMain(Long facilityId) {
        Long dailyDietId = dailyDietReader.getLatest(facilityId);
        DailyDietImageResponse dailyDietImageResponse = dailyDietImageReader.getLatest(dailyDietId);
        return new DailyDietMainResponse(dailyDietId, dailyDietImageResponse.id(), dailyDietImageResponse.dailyDietType(), dailyDietImageResponse.imageUrl());
    }

    public DailyDietResponse read(Long dailyDietId) {
        DailyDiet dailyDiet = dailyDietReader.get(dailyDietId);
        List<DailyDietImageResponse> dailyDietImageResponses = dailyDietImageReader.get(dailyDietId);
        return new DailyDietResponse(dailyDiet.getContent(), dailyDiet.getRecordDate(), dailyDiet.getCreatedAt(), dailyDiet.getUpdatedAt(), dailyDietImageResponses);
    }

}
