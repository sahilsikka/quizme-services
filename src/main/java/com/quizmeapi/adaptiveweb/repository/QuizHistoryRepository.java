package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.QuizHistory;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Integer> {
    List<QuizHistory> findAllByUser(User user);
    Page<QuizHistory> findByUserOrderByTimestampDesc(User user, Pageable pageable);
    QuizHistory findByQuizId(int id);
}
