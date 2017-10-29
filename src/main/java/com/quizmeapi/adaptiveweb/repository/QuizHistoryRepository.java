package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.QuizHistory;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Integer> {
    Iterable<QuizHistory> findAllByUser(User user);
    QuizHistory findByQuizId(int id);
}
