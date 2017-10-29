package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.QuizSession;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Integer> {
    QuizSession findByUser(User user);
    QuizSession findByQuizId(int quizId);
}
