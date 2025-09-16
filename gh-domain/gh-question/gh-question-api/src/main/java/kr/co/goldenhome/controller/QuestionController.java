package kr.co.goldenhome.controller;

import kr.co.goldenhome.dto.QuestionResponse;
import kr.co.goldenhome.dto.QuestionSurveyRequest;
import kr.co.goldenhome.dto.QuestionSurveyResponse;
import kr.co.goldenhome.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<QuestionResponse> readAll() {
        return questionService.readAll();
    }

    @GetMapping("/survey")
    public QuestionSurveyResponse survey(@RequestBody QuestionSurveyRequest request) {
        return questionService.survey(request);
    }
}
