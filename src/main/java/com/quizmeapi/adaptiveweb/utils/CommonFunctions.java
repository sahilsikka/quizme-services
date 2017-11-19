package com.quizmeapi.adaptiveweb.utils;

import com.quizmeapi.adaptiveweb.model.Question;
import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import com.quizmeapi.adaptiveweb.repository.QuizRepository;
import com.quizmeapi.adaptiveweb.variables.GlobalStaticVariables;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonFunctions extends GlobalStaticVariables{

    private static QuizRepository quizRepository;
    private static QuestionRepository questionRepository;

    @Autowired
    public CommonFunctions(QuizRepository quizRepository, QuestionRepository questionRepository) {
        CommonFunctions.quizRepository = quizRepository;
        CommonFunctions.questionRepository = questionRepository;
    }

    public static int calculateScore(int quizId) {
        Iterable<Quiz> questionsAnswered = quizRepository.findAllByQuizId(quizId);
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

    public static double getUserAbility(int score) {
        return score/quizQuestionCount;
    }

}
