package kr.co.goldenhome.dto;

import kr.co.goldenhome.entity.DailyRehabilitation;

import java.time.LocalDate;
import java.util.List;

public record DailyRehabilitationResponse(
        Long id,
        LocalDate recordDate,
        String treatment,
        List<DailyExerciseResponse> dailyExerciseResponses
) {

    public static DailyRehabilitationResponse of(DailyRehabilitation dailyRehabilitation, List<DailyExerciseResponse> dailyExerciseResponses) {
        return new DailyRehabilitationResponse(dailyRehabilitation.getId(), dailyRehabilitation.getRecordDate(), dailyRehabilitation.getTreatment(), dailyExerciseResponses);
    }
}
