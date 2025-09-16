package kr.co.goldenhome.service;

import kr.co.goldenhome.dto.QuestionDomainOptionRequest;
import kr.co.goldenhome.dto.QuestionDomainRequest;
import kr.co.goldenhome.dto.QuestionRequest;
import kr.co.goldenhome.dto.ScoreConversionRequest;
import kr.co.goldenhome.entity.Question;
import kr.co.goldenhome.entity.QuestionDomain;
import kr.co.goldenhome.entity.QuestionDomainOption;
import kr.co.goldenhome.entity.ScoreConversion;
import kr.co.goldenhome.repository.QuestionDomainOptionRepository;
import kr.co.goldenhome.repository.QuestionDomainRepository;
import kr.co.goldenhome.repository.QuestionRepository;
import kr.co.goldenhome.repository.ScoreConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionInitializationService {

    private final QuestionDomainRepository questionDomainRepository;
    private final QuestionDomainOptionRepository questionDomainOptionRepository;
    private final ScoreConversionRepository scoreConversionRepository;
    private final QuestionRepository questionRepository;

    public void createQuestionDomain(QuestionDomainRequest request) {
        questionDomainRepository.save(QuestionDomain.create(request.name()));
    }

    public void createQuestionDomainOption(QuestionDomainOptionRequest request) {
        questionDomainOptionRepository.save(QuestionDomainOption.create(request.name(), request.originalScore(), request.questionDomainId()));
    }

    public void create(List<QuestionRequest> requests) {
        List<Question> questions = requests.stream().map(request -> Question.create(request.questionDomainId(), request.content())).toList();
        questionRepository.saveAll(questions);
    }

    public void createScoreConversion(ScoreConversionRequest request) {
        scoreConversionRepository.save(ScoreConversion.create(request.questionDomainId(), request.originalSum(), request.convertedSum()));
    }

    public void createScoreConversions(List<ScoreConversionRequest> requests) {
        List<ScoreConversion> scoreConversions = requests.stream()
                .map(request -> ScoreConversion.create(request.questionDomainId(), request.originalSum(), request.convertedSum()))
                .toList();
        scoreConversionRepository.saveAll(scoreConversions);
    }
}
