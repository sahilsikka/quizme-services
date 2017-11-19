package com.quizmeapi.adaptiveweb.controller;

import com.quizmeapi.adaptiveweb.model.QuizHistory;
import com.quizmeapi.adaptiveweb.model.QuizSession;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.*;
import com.quizmeapi.adaptiveweb.utils.CommonFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/quizHistory", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class QuizHistoryController {
    private final QuizHistoryRepository quizHistoryRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizSessionRepository quizSessionRepository;
    private CommonFunctions commonFunctions;

    @Autowired
    public QuizHistoryController(QuizHistoryRepository quizHistoryRepository, UserRepository userRepository, QuestionRepository questionRepository, QuizRepository quizRepository, QuestionRepository questionRepository1, QuizSessionRepository quizSessionRepository, CommonFunctions commonFunctions) {
        this.quizHistoryRepository = quizHistoryRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository1;
        this.quizSessionRepository = quizSessionRepository;
        this.commonFunctions = commonFunctions;
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<QuizHistory> getQuizByUserId(@PathVariable("user_id") int userId) {
        User user = userRepository.findById(userId);
        return quizHistoryRepository.findAllByUser(user);
    }

    @RequestMapping(value = "/{quiz_id}", method = RequestMethod.GET)
    @ResponseBody
    public QuizHistory getQuizScore(@PathVariable("quiz_id") int quizId) {
        int score = commonFunctions.calculateScore(quizId);
        QuizSession quizSession = quizSessionRepository.findByQuizId(quizId);
        User user = quizSession.getUser();
        QuizHistory quizHistory = new QuizHistory();
        quizHistory.setScore(score);
        quizHistory.setQuizId(quizId);
        quizHistory.setUser(user);
        quizHistoryRepository.save(quizHistory);
        return quizHistory;
    }

}
