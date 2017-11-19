package com.quizmeapi.adaptiveweb.utils;

import com.quizmeapi.adaptiveweb.model.Question;
import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.model.QuizHistory;
import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import com.quizmeapi.adaptiveweb.repository.QuizRepository;
import com.quizmeapi.adaptiveweb.variables.GlobalStaticVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommonFunctions extends GlobalStaticVariables{

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public CommonFunctions(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public int calculateScore(int quizId) {
        List<Quiz> questionsAnswered = quizRepository.findByQuizId(quizId);
        int score = 0;
        for (Quiz quiz: questionsAnswered) {
            int questionId = quiz.getQuestion().getId();
            String userChoice = quiz.getUserChoice();
            Question question = questionRepository.findById(questionId);
            if (userChoice.equalsIgnoreCase(question.getAnswer())) {
                score++;
            }
        }
        return score;
    }

    private static double getUserAbility(int score) {
        return (double) score/quizQuestionCount;
    }

    public static double calculateMeanAbility(List<QuizHistory> quizHistories) {
        double sum = 0;
        for (QuizHistory quizHistory: quizHistories) {
            sum += getUserAbility(quizHistory.getScore());
        }
        return sum / quizHistories.size();
    }

    public Map<Integer, Double> calculateProbability(double meanAbility) {
        List<Question> questions = questionRepository.findAll();
        HashMap<Integer, Double> map = new HashMap<>();
        for (Question question: questions) {
            map.put(question.getId(), Math.abs(meanAbility - computeProbabilityUtil(meanAbility, question.getDifficulty())));
        }
        return sortByValue(map);
    }

    private double computeProbabilityUtil(double meanAbility, double diff) {
        double eValue = Math.pow(2.71828, (meanAbility - diff));
        return eValue / (1 + eValue);
    }

    private static <Integer, Double extends Comparable<? super Double>> Map<Integer,
            Double> sortByValue(HashMap<Integer, Double> unsortMap) {
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<>(unsortMap.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
