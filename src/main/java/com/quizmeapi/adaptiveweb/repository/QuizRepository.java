package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Quiz;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByQuizId(int quizId);
    List<Quiz> findAllByQuizIdAndUser(int quizId, User user);
    Page<Quiz> findAllByUserChoiceNotNull(Pageable pageable);
}
