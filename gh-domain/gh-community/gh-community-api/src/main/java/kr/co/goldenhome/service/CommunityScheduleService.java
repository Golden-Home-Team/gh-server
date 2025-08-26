package kr.co.goldenhome.service;

import exception.CustomException;
import exception.ErrorCode;
import kr.co.goldenhome.dto.CommunityScheduleRequest;
import kr.co.goldenhome.dto.CommunityScheduleResponse;
import kr.co.goldenhome.dto.CommunityScheduleUpdateRequest;
import kr.co.goldenhome.entity.CommunitySchedule;
import kr.co.goldenhome.repository.CommunityScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityScheduleService {

    private final CommunityScheduleRepository communityScheduleRepository;

    public void write(CommunityScheduleRequest request, Long facilityId) {
        communityScheduleRepository.save(CommunitySchedule.create(facilityId, request.recordDate(), request.content()));
    }

    @Transactional
    public void update(CommunityScheduleUpdateRequest request, Long scheduleId) {
        CommunitySchedule communitySchedule = communityScheduleRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND, "CommunityScheduleService.update"));
        communitySchedule.update(request.content());
    }

    public List<CommunityScheduleResponse> readByMonth(Long facilityId, Month month) {
        return communityScheduleRepository.getByFacilityIdAndMonth(facilityId, month.getValue())
                .stream().map(CommunityScheduleResponse::from).toList();
    }
}
