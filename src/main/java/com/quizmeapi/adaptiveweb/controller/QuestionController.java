package com.quizmeapi.adaptiveweb.controller;

import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/question", produces = "application/json")
public class QuestionController {
    private QuestionRepository questionRepository;

    QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("/all")
    @ResponseBody
    @CrossOrigin
    public Iterable<?> getAllQuestions() {
        return questionRepository.findAll();
    }


}
