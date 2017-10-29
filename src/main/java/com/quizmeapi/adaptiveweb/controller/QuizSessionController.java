package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quizmeapi.adaptiveweb.model.QuizSession;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.QuizHistoryRepository;
import com.quizmeapi.adaptiveweb.repository.QuizSessionRepository;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(value = "/quizSession", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizSessionController {

    private final QuizSessionRepository quizSessionRepository;
    private final UserRepository userRepository;
    private final QuizHistoryRepository quizHistoryRepository;

    @Autowired
    public QuizSessionController(QuizSessionRepository quizSessionRepository, UserRepository userRepository, QuizHistoryRepository quizHistoryRepository) {
        this.quizSessionRepository = quizSessionRepository;
        this.userRepository = userRepository;
        this.quizHistoryRepository = quizHistoryRepository;
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public ObjectNode[] startUserQuizSession(@PathVariable("user_id") int userId) {
        Random random = new Random();
        int n = 100000 + random.nextInt() * 900000;
        while (quizHistoryRepository.findByQuizId(n) != null) {
            n = 100000 + random.nextInt() * 900000;
        }
        User user = userRepository.findById(userId);
        if (quizSessionRepository.findByUser(user) != null) {
            quizSessionRepository.deleteByUser(user);
        }
        QuizSession quizSession = new QuizSession();
        quizSession.setQuizId(n);
        quizSession.setUser(user);
        quizSessionRepository.save(quizSession);
        ObjectNode[] objectNodes = new ObjectNode[1];
        objectNodes[0].put("userId", String.valueOf(user.getId()));
        objectNodes[0].put("quizId", String.valueOf(n));
        return objectNodes;
    }
}
