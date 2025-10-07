package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.QuestionDomainOptionInnerResponse;
import kr.co.goldenhome.dto.QuestionResponse;
import kr.co.goldenhome.dto.QuestionSurveyRequest;
import kr.co.goldenhome.dto.QuestionSurveyResponse;
import kr.co.goldenhome.entity.Question;
import kr.co.goldenhome.entity.QuestionDomainOption;
import kr.co.goldenhome.entity.ScoreConversion;
import kr.co.goldenhome.exception.CustomException;
import kr.co.goldenhome.exception.ErrorCode;
import kr.co.goldenhome.repository.QuestionDomainOptionRepository;
import kr.co.goldenhome.repository.QuestionRepository;
import kr.co.goldenhome.repository.ScoreConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionDomainOptionRepository questionDomainOptionRepository;
    private final ScoreConversionRepository scoreConversionRepository;

    public List<QuestionResponse> readAll() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream()
                .map(question -> {
                    List<QuestionDomainOption> questionDomainOptions = questionDomainOptionRepository.findByQuestionDomainId(question.getQuestionDomainId());
                    List<QuestionDomainOptionInnerResponse> questionDomainOptionInnerResponses = questionDomainOptions.stream().map(questionDomainOption -> new QuestionDomainOptionInnerResponse(questionDomainOption.getId(), questionDomainOption.getName(), questionDomainOption.getOriginalScore())).toList();
                    return new QuestionResponse(question.getQuestionDomainId(), question.getContent(), questionDomainOptionInnerResponses);
                }).toList();
    }

    public QuestionSurveyResponse survey(QuestionSurveyRequest request) {

        Map<Long, Integer> originalScores = new HashMap<>();
        for (Long questionDomainOptionId : request.questionDomainOptionIds()) {
            QuestionDomainOption questionDomainOption = questionDomainOptionRepository.findById(questionDomainOptionId).orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND, "QuestionService.survey"));
            Long questionDomainId = questionDomainOption.getQuestionDomainId();
            if (questionDomainId == 6L) {
                questionDomainId = 5L;
            }
            originalScores.merge(questionDomainId, questionDomainOption.getOriginalScore(), Integer::sum);
        }

        double finalScore = 0;
        for (Map.Entry<Long, Integer> entry : originalScores.entrySet()) {
            Long questionDomainId = entry.getKey();
            int originalSum = entry.getValue();
            ScoreConversion scoreConversion = scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(questionDomainId, originalSum).orElseThrow(() -> new CustomException(ErrorCode.SCORE_NOT_FOUND, "QuestionService.survey"));
            finalScore += scoreConversion.getConvertedSum();
        }

        String grade = getGrade(finalScore);
        return new QuestionSurveyResponse(grade, finalScore);

    }

    private String getGrade(double finalScore) {
        if (finalScore >= 95 ) return "1등급";
        else if (finalScore >= 75) return "2등급";
        else if (finalScore >= 60) return "3등급";
        else if (finalScore >= 51) return "4등급";
        else if (finalScore >= 45) return "5등급";
        return"인지지원등급";
    }
}
