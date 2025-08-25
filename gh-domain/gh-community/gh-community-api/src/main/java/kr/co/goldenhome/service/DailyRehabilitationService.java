package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.DailyExerciseResponse;
import kr.co.goldenhome.dto.DailyRehabilitationRequest;
import kr.co.goldenhome.dto.DailyRehabilitationResponse;
import kr.co.goldenhome.dto.DailyRehabilitationUpdateRequest;
import kr.co.goldenhome.entity.DailyExercise;
import kr.co.goldenhome.entity.DailyRehabilitation;
import kr.co.goldenhome.repository.DailyExerciseRepository;
import kr.co.goldenhome.repository.DailyRehabilitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyRehabilitationService {

    private final DailyRehabilitationRepository dailyRehabilitationRepository;
    private final DailyExerciseRepository dailyExerciseRepository;

    @Transactional
    public void write(DailyRehabilitationRequest request, Long facilityId) {
        DailyRehabilitation dailyRehabilitation = dailyRehabilitationRepository.save(DailyRehabilitation.create(facilityId, request.recordDate(), request.treatment()));
        List<DailyExercise> dailyExercises = request.dailyExerciseRequests().stream().map(dailyExerciseRequest -> DailyExercise.create(dailyRehabilitation.getId(), dailyExerciseRequest.content(), dailyExerciseRequest.startTime(), dailyExerciseRequest.endTime())).toList();
        dailyExerciseRepository.saveAll(dailyExercises);
    }

    @Transactional
    public void update(DailyRehabilitationUpdateRequest request, Long dailyRehabId) {
        DailyRehabilitation dailyRehabilitation = dailyRehabilitationRepository.findById(dailyRehabId).orElseThrow(() -> new CustomException(ErrorCode.DAILY_REHAB_NOT_FOUND, "DailyRehabilitationService.update"));
        dailyRehabilitation.update(request.treatment());
        request.dailyExerciseUpdateRequests().forEach(dailyExerciseUpdateRequest -> {
            DailyExercise dailyExercise = dailyExerciseRepository.findById(dailyExerciseUpdateRequest.dailyExerciseId()).orElseThrow(() -> new CustomException(ErrorCode.DAILY_REHAB_NOT_FOUND, "DailyExerciseService.update.exercise"));
            dailyExercise.update(dailyExerciseUpdateRequest.content(), dailyExerciseUpdateRequest.startTime(), dailyExerciseUpdateRequest.endTime());
        });
    }

    public DailyRehabilitationResponse readByDayOfWeek(Long facilityId, DayOfWeek dayOfWeek) {
        DailyRehabilitation dailyRehabilitation = dailyRehabilitationRepository.getLatestByFacilityIdAndDayOfWeek(facilityId, dayOfWeek.getValue());
        List<DailyExerciseResponse> dailyExerciseResponses = dailyExerciseRepository.findAllByDailyRehabilitationId(dailyRehabilitation.getId()).stream().map(dailyExercise -> new DailyExerciseResponse(dailyRehabilitation.getId(), dailyExercise.getContent(), dailyExercise.getStartTime(), dailyExercise.getEndTime())).toList();
        return DailyRehabilitationResponse.of(dailyRehabilitation, dailyExerciseResponses);
    }
}
