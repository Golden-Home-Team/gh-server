package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.DailyMedicationRequest;
import kr.co.goldenhome.dto.DailyMedicationResponse;
import kr.co.goldenhome.dto.DailyMedicationUpdateRequest;
import kr.co.goldenhome.entity.DailyMedication;
import kr.co.goldenhome.repository.DailyMedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class DailyMedicationService {

    private final DailyMedicationRepository dailyMedicationRepository;

    public void write(DailyMedicationRequest request, Long facilityId) {
        dailyMedicationRepository.save(DailyMedication.create(facilityId, request.recordDate(), request.morningContent(), request.afternoonContent(), request.nightContent()));
    }

    @Transactional
    public void update(DailyMedicationUpdateRequest request, Long dailyMedicationId) {
        DailyMedication dailyMedication = dailyMedicationRepository.findById(dailyMedicationId).orElseThrow(() -> new CustomException(ErrorCode.DAILY_MEDICATION_NOT_FOUND, "DailyMedicationService.update"));
        dailyMedication.update(request.morningContent(), request.afternoonContent(), request.nightContent());
    }

    public DailyMedicationResponse readByDayOfWeek(Long facilityId, DayOfWeek dayOfWeek) {
        DailyMedication dailyMedication = dailyMedicationRepository.getLatestByFacilityIdAndDayOfWeek(facilityId, dayOfWeek.getValue());
        return DailyMedicationResponse.from(dailyMedication);
    }
}
