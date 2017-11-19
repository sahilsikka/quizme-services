package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Iterable<Quiz> findAllByQuizId(int quizId);
    List<Quiz> findAllByQuizIdAndUser(int quizId, User user);
    Page<Quiz> findAllByUserChoiceNotNull(Pageable pageable);
}
