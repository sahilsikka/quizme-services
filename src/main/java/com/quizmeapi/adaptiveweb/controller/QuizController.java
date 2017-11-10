package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quizmeapi.adaptiveweb.model.Question;
import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.model.QuizSession;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizController {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public QuizController(QuizRepository quizRepository, UserRepository userRepository, QuestionRepository questionRepository, QuizSessionRepository quizSessionRepository, QuizHistoryRepository quizHistoryRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.quizSessionRepository = quizSessionRepository;
        this.quizHistoryRepository = quizHistoryRepository;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @CrossOrigin
    @ResponseBody
    public List<Quiz> getQuizQuestions(@RequestHeader("quiz_id") int quizId, @RequestHeader("user_id") int userId) {
        User user = userRepository.findById(userId);
        return quizRepository.findAllByQuizIdAndUser(quizId, user);
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public ObjectNode getUserQuiz(@PathVariable("user_id") int userId) throws JSONException {
        Page<Question> questions = questionRepository.findAll(new PageRequest(0, 15));
        List<Question> quizSet = questions.getContent();
        int quizId = getQuizId(userId);
        ArrayNode arrayNode = objectMapper.valueToTree(quizSet);
        ObjectNode res = objectMapper.createObjectNode();
        res.put("quiz_id", quizId);
        res.put("questions", arrayNode);
        return res;
    }

    private int getQuizId(int userId) {
        Random random = new Random();
        int n = 100000 + random.nextInt() * 900000;
        if (n < 0) {
            n = -n;
        }
        while (quizHistoryRepository.findByQuizId(n) != null) {
            n = 100000 + random.nextInt() * 900000;
        }
        User user = userRepository.findById(userId);
        QuizSession oldQuizSession = quizSessionRepository.findByUser(user);
        if (oldQuizSession != null) {
            oldQuizSession.setQuizId(n);
        } else {
            QuizSession quizSession = new QuizSession();
            quizSession.setQuizId(n);
            quizSession.setUser(user);
            quizSessionRepository.save(quizSession);
        }
        return n;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> postUserResponse(@RequestBody JsonNode jsonNode) throws IOException {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            Quiz quiz = new Quiz();
            if (!jsonNode.has("user_id") && !jsonNode.has("quiz_id") && !jsonNode.has("answers")) {
                objectNode.put("status", "Bad request. Missing user_id, quiz_id or answers.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
            }
            ArrayNode arrayNode = (ArrayNode) jsonNode.get("answers");
            for (JsonNode userRespnse : arrayNode) {
                if (!userRespnse.has("question_id") && !userRespnse.has("user_choice") && !userRespnse.has("time_taken")) {
                    objectNode.put("status", "Bad request. Missing question_id, user_choice or time_taken.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
                } else {
                    User user = userRepository.findById(jsonNode.get("user_id").asInt());
                    Question question = questionRepository.findById(userRespnse.get("question_id").asInt());
                    int timeTaken = userRespnse.get("time_taken").asInt();
                    quiz.setQuizId(jsonNode.get("quiz_id").asInt());
                    quiz.setUserChoice(userRespnse.get("user_choice").asText());
                    quiz.setQuestion(question);
                    quiz.setUser(user);
                    quiz.setTimeTaken(timeTaken);
                    quizRepository.save(quiz);
                }
            }
            objectNode.put("status", "Success");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(objectNode);
        } catch (Exception e) {
            System.out.println(e);
            objectNode.put("status", "Cannot parse request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
        }
    }


}
