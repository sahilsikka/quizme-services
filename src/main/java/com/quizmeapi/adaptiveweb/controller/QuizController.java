package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quizmeapi.adaptiveweb.model.*;
import com.quizmeapi.adaptiveweb.repository.*;
import com.quizmeapi.adaptiveweb.utils.CommonFunctions;
import com.quizmeapi.adaptiveweb.variables.GlobalStaticVariables;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class QuizController extends GlobalStaticVariables{

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private ObjectMapper objectMapper;
    private CommonFunctions commonFunctions;

    @Autowired
    public QuizController(QuizRepository quizRepository, UserRepository userRepository, QuestionRepository questionRepository, QuizSessionRepository quizSessionRepository, QuizHistoryRepository quizHistoryRepository, CommonFunctions commonFunctions) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.quizSessionRepository = quizSessionRepository;
        this.quizHistoryRepository = quizHistoryRepository;
        this.objectMapper = new ObjectMapper();
        this.commonFunctions = commonFunctions;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    public List<Quiz> getQuizQuestions(@RequestHeader("quiz_id") int quizId, @RequestHeader("user_id") int userId) {
        User user = userRepository.findById(userId);
        return quizRepository.findAllByQuizIdAndUser(quizId, user);
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode getUserQuiz(@PathVariable("user_id") int userId) throws JSONException {
        User user = userRepository.findById(userId);
        List<QuizHistory> quizHistories = quizHistoryRepository.findAllByUser(user);
        List<Question> quiz;
        if (quizHistories.size() == 0) {
            // Initial Quiz (NOT - ADAPTIVE)
            quiz = getFirstQuiz();
        } else {
            //ADAPTIVE QUIZ
            quiz = getAdaptiveQuiz(user);
        }
        int quizId = getQuizId(userId);
        ArrayNode arrayNode = objectMapper.valueToTree(quiz);
        ObjectNode res = objectMapper.createObjectNode();
        res.put("quiz_id", quizId);
        res.put("questions", arrayNode);
        return res;
    }

    private List<Question> getAdaptiveQuiz(User user) {
        List<Question> quiz = new ArrayList<>();
        List<QuizHistory> quizzes = quizHistoryRepository.findAllByUser(user);
        double meanAbility = commonFunctions.calculateMeanAbility(quizzes);
        Map<Integer, Double> map = commonFunctions.calculateProbability(meanAbility);
        int count = 0;
        for (Integer i: map.keySet()) {
            quiz.add(questionRepository.findById(i));
            count++;
            if (count == quizQuestionCount) {
                break;
            }
        }
        return quiz;
    }

    private List<Question> getFirstQuiz() {
        List<Question> initialQuestions = new ArrayList<>();
        for (Integer i: initialQuiz) {
            initialQuestions.add(questionRepository.findById(i));
        }
        return initialQuestions;
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
    public ResponseEntity<?> postUserResponse(@RequestBody JsonNode jsonNode) throws IOException {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            if (!jsonNode.has("user_id") && !jsonNode.has("quiz_id") && !jsonNode.has("answers")) {
                objectNode.put("status", "Bad request. Missing user_id, quiz_id or answers.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
            }
            int quizId = jsonNode.get("quiz_id").asInt();
            int userId = jsonNode.get("user_id").asInt();
            if (userRepository.findById(userId) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
            }
            User user = userRepository.findById(userId);
            ArrayNode arrayNode = (ArrayNode) jsonNode.get("answers");
            for (JsonNode userRespnse : arrayNode) {
                if (!userRespnse.has("question_id") && !userRespnse.has("user_choice") && !userRespnse.has("time_taken")) {
                    objectNode.put("status", "Bad request. Missing question_id, user_choice or time_taken.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
                } else {
                    Quiz quiz = new Quiz();
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
            int score = commonFunctions.calculateScore(quizId);
            QuizHistory quizHistory = new QuizHistory();
            quizHistory.setScore(score);
            quizHistory.setQuizId(quizId);
            quizHistory.setUser(user);
            quizHistoryRepository.save(quizHistory);
            objectNode.put("status", "Success");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(objectNode);
        } catch (Exception e) {
            System.out.println(e);
            objectNode.put("status", "Cannot parse request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode);
        }
    }


}
