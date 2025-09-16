package kr.co.goldenhome.service

import kr.co.goldenhome.dto.QuestionSurveyRequest
import kr.co.goldenhome.entity.Question
import kr.co.goldenhome.entity.QuestionDomainOption
import kr.co.goldenhome.entity.ScoreConversion
import kr.co.goldenhome.repository.QuestionDomainOptionRepository
import kr.co.goldenhome.repository.QuestionRepository
import kr.co.goldenhome.repository.ScoreConversionRepository
import spock.lang.Specification

class QuestionServiceSpec extends Specification {

    QuestionService questionService
    QuestionRepository questionRepository = Mock()
    QuestionDomainOptionRepository questionDomainOptionRepository = Mock()
    ScoreConversionRepository scoreConversionRepository = Mock()

    def "setup"() {
        questionService = new QuestionService(questionRepository, questionDomainOptionRepository, scoreConversionRepository)
    }

    def "readAll - questionRepository, questionDomainOptionRepository 를 호출한다"() {

        when:
        questionService.readAll()

        then:
        1 * questionRepository.findAll() >> List.of(Question.create(1L, "혼자서 거동이 가능하신가요?"))
        1 * questionDomainOptionRepository.findByQuestionDomainId(_) >> List.of(QuestionDomainOption.builder().id(1L).name("완전자립").originalScore(1).questionDomainId(1L).build())

    }

    def "survey - questionDomainOptionRepository, scoreConversionRepository 를 호출한다"() {
        given:
        def givenQuestionDomainOptionId = 1L
        def givenRequest = new QuestionSurveyRequest(List.of(givenQuestionDomainOptionId))
        def expectedQuestionDomainOption = QuestionDomainOption.create("완전자립", 1, 1)
        def expectedScoreConversion = ScoreConversion.builder().convertedSum(10).build()
        when:
        questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(givenQuestionDomainOptionId) >> Optional.of(expectedQuestionDomainOption)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(*_) >> Optional.of(expectedScoreConversion)
    }

}
