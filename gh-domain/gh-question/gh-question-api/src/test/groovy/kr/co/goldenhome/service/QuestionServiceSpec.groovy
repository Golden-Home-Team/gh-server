package kr.co.goldenhome.service

import kr.co.goldenhome.dto.QuestionSurveyRequest
import kr.co.goldenhome.entity.Question
import kr.co.goldenhome.entity.QuestionDomainOption
import kr.co.goldenhome.entity.ScoreConversion
import kr.co.goldenhome.exception.CustomException
import kr.co.goldenhome.exception.ErrorCode
import kr.co.goldenhome.repository.QuestionDomainOptionRepository
import kr.co.goldenhome.repository.QuestionRepository
import kr.co.goldenhome.repository.ScoreConversionRepository
import spock.lang.Specification
import spock.lang.Unroll

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

    def "여러_답변_옵션ID가_주어졌을때_올바른_점수를_계산한다"() {
        given:
        def givenQuestionDomainOptionIds = List.of(1L, 2L, 3L) // 예시 ID
        def givenRequest = new QuestionSurveyRequest(givenQuestionDomainOptionIds)

        def option1 = QuestionDomainOption.create("완전자립", 1, 1L)
        def option2 = QuestionDomainOption.create("부분도움", 2, 1L)
        def option3 = QuestionDomainOption.create("완전도움", 3, 1L)

        def originalSum = option1.getOriginalScore() + option2.getOriginalScore() + option3.getOriginalScore() // 1 + 2 + 3 = 6
        def expectedConvertedScore = 55.0

        def scoreConversion = ScoreConversion.builder()
                .convertedSum(expectedConvertedScore)
                .build()

        when:
        def response = questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(1L) >> Optional.of(option1)
        1 * questionDomainOptionRepository.findById(2L) >> Optional.of(option2)
        1 * questionDomainOptionRepository.findById(3L) >> Optional.of(option3)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(1L, originalSum) >> Optional.of(scoreConversion)
        (double) response.finalScore() == expectedConvertedScore
        response.grade() == "4등급"
    }

    def "유효하지_않은_답변_옵션ID가_주어졌을때_예외를_던진다"() {
        given:
        def invalidId = 99L
        def givenRequest = new QuestionSurveyRequest(List.of(invalidId))

        when:
        questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(invalidId) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.OPTION_NOT_FOUND
    }

    def "일치하는_점수_변환_규칙이_없을때_예외를_던진다"() {
        given:
        def givenId = 1L
        def givenRequest = new QuestionSurveyRequest(List.of(givenId))
        def option = QuestionDomainOption.create("완전자립", 1, 1L)

        when:
        questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(givenId) >> Optional.of(option)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(1L, 1) >> Optional.empty()
        def e = thrown(CustomException)
        e.errorCode == ErrorCode.SCORE_NOT_FOUND
    }

    @Unroll
    def "다른_영역의_옵션들이_주어졌을때_각_영역의_점수를_올바르게_계산한다"() {
        given:
        def givenRequest = new QuestionSurveyRequest(List.of(1L, 4L))

        def option1 = QuestionDomainOption.create("완전자립", 1, 1L) // 도메인 ID 1
        def option4 = QuestionDomainOption.create("예", 1, 2L) // 도메인 ID 2

        def conversion1 = ScoreConversion.builder().convertedSum(20.0).build()
        def conversion2 = ScoreConversion.builder().convertedSum(30.0).build()
        def expectedFinalScore = 50.0

        when:
        def response = questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(1L) >> Optional.of(option1)
        1 * questionDomainOptionRepository.findById(4L) >> Optional.of(option4)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(1L, 1) >> Optional.of(conversion1)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(2L, 1) >> Optional.of(conversion2)
        response.finalScore() == (double) expectedFinalScore
        response.grade() == "5등급"
    }

    @Unroll
    def "최종_점수에_따라_올바른_등급을_반환한다#finalScore"() {
        given:
        def givenId = 1L
        def givenRequest = new QuestionSurveyRequest(List.of(givenId))
        def option = QuestionDomainOption.create("완전자립", 1, 1L)
        def scoreConversion = ScoreConversion.builder().convertedSum(finalScore).build()

        when:
        def response = questionService.survey(givenRequest)

        then:
        1 * questionDomainOptionRepository.findById(givenId) >> Optional.of(option)
        1 * scoreConversionRepository.findByQuestionDomainIdAndOriginalSum(1L, 1) >> Optional.of(scoreConversion)
        response.grade() == expectedGrade

        where:
        finalScore | expectedGrade
        95.0       | "1등급"
        80.0       | "2등급"
        74.9       | "3등급"
        60.0       | "3등급"
        51.0       | "4등급"
        45.0       | "5등급"
        44.9       | "인지지원등급"
    }

}
