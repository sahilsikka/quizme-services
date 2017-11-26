package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import com.quizmeapi.adaptiveweb.repository.QuizRepository;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/analytics", produces = "application/json")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public AnalyticsController(QuizRepository quizRepository, QuestionRepository questionRepository,
                               UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseBody
    public ArrayNode getUserBarChartCategoryWise(@RequestHeader(name = "X-user-id") int userId) {
        Map<String, Integer> categoryMap = new HashMap<>();
        Map<String, Integer> totalMap = new HashMap<>();
        if (userRepository.findById(userId) == null) {
            return null;
        }
        User user = userRepository.findById(userId);
        List<Quiz> questionList = quizRepository.findDistinctByUser(user);
        List<Quiz> allQuiz = quizRepository.findAllByUser(user);
        Set<String> categories = new HashSet<>();
        for (Quiz quiz: questionList) {
            String category = quiz.getQuestion().getCategory();
            categories.add(category);
            List<Quiz> quizList = quizRepository.findAllByQuestionAndUserOrderByTimeStampDesc(quiz.getQuestion(), user,
                    new PageRequest(0,1)).getContent();
            if (quizList != null) {
                Quiz quiz1 = quizList.get(0);
                if (quiz1.getUserChoice().equalsIgnoreCase(quiz1.getQuestion().getAnswer())) {
                    if (categoryMap.containsKey(category)) {
                        categoryMap.put(category, categoryMap.get(category) + 1);
                    } else {
                        categoryMap.put(category, 1);
                    }
                }
            }
        }
        Set<Integer> questionIds = new HashSet<>();
        for (Quiz quiz: allQuiz) {
            int questionId = quiz.getQuestion().getId();
            if (questionIds.contains(questionId)) {
                continue;
            }
            questionIds.add(questionId);
            String category = quiz.getQuestion().getCategory();
            if (totalMap.containsKey(category)) {
                totalMap.put(category, totalMap.get(category) + 1);
            } else {
                totalMap.put(category, 1);
            }
        }
        List<ObjectNode> objectNodes = new ArrayList<>();
        for (String category: categories) {
            int correct = 0;
            if (categoryMap.get(category) != null) {
                correct = categoryMap.get(category);
            }
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("category", category);
            objectNode.put("correct", correct);
            objectNode.put("attempted", totalMap.get(category));
            objectNodes.add(objectNode);
        }
        return objectMapper.valueToTree(objectNodes);
    }
}
