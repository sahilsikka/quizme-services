package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/question", produces = "application/json")
public class QuestionController {
    private QuestionRepository questionRepository;
    private ObjectMapper objectMapper;

    QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/all")
    @ResponseBody
    @CrossOrigin
    Iterable<?> getAllQuestions() {
        return questionRepository.findAll();
    }


}
