package com.quizmeapi.adaptiveweb.utils;

import com.quizmeapi.adaptiveweb.model.Question;
import com.quizmeapi.adaptiveweb.model.Quiz;
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

    public Map<String, ArrayList<String>> nextQuestionList(int quizId) {
        Map<String, Double> categorySet = calculateProficiency(quizId);
        Map<String, ArrayList<String>> nextQuestion = new HashMap<>();

        List<Quiz> questionsAnswered = quizRepository.findByQuizId(quizId);

        for (Quiz quiz: questionsAnswered) {
            int questionId = quiz.getQuestion().getId();
            Question question = questionRepository.findById(questionId);
            nextQuestion.put(question.getCategory(), new ArrayList<String>(){{
                add(getNextLevel(question.getLevels() , categorySet.get(question.getCategory())));
            }
            });
        }

        return nextQuestion;
    }

    public  Map<String, Double> calculateProficiency(int quizId){
        Map<String, Double> categorySet = new HashMap<>();
        for (String topic : topics){
            categorySet.put(topic, 0.0);
        }

        List<Quiz> questionsAnswered = quizRepository.findByQuizId(quizId);

        for (Quiz quiz: questionsAnswered) {
            int questionId = quiz.getQuestion().getId();
            String userChoice = quiz.getUserChoice();
            Question question = questionRepository.findById(questionId);
            if (userChoice.equalsIgnoreCase(question.getAnswer())) {
                categorySet.put(question.getCategory(), categorySet.get(question.getCategory()) + 1);
            }
        }

        normalizedValues(categorySet);
        return categorySet;
    }




    private void normalizedValues(Map<String, Double> categorySet) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for(Double values : categorySet.values()){
            min = Math.min(min , values/quizQuestionCount);
            max = Math.max(max, values/quizQuestionCount);
        }

        for(Map.Entry<String, Double> entry : categorySet.entrySet()){
            categorySet.put(entry.getKey() , (entry.getValue()/quizQuestionCount - min) / (max - min));
        }
    }

    private String getNextLevel(String level, Double normalizedValue) {
        HashMap<String, String> probableLevels = nextLevel.get(level);
        for(Map.Entry<String , String> entry : probableLevels.entrySet()){
            double min = Double.parseDouble(entry.getKey().split("/")[0]);
            double max = Double.parseDouble(entry.getKey().split("/")[1]);
            if(normalizedValue >= min && normalizedValue <= max)
                return entry.getValue();
        }
        return "";
    }


/*

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
            map.put(question.getId(), computeProbabilityUtil(meanAbility, question.getDifficulty()));
        }
        return sortByValue(map);
    }

    private double computeProbabilityUtil(double meanAbility, double diff) {
        double eValue = Math.pow(2.71828, (meanAbility - diff));
        System.out.println(meanAbility + " " + eValue/(1+eValue));
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

    */

}
