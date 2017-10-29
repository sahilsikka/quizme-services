package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Iterable<Quiz> findAllByQuizId(int quizId);
}
