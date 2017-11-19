package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.QuizHistory;
import com.quizmeapi.adaptiveweb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import java.util.List;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Integer> {
    List<QuizHistory> findAllByUser(User user);
    QuizHistory findByQuizId(int id);
    Page<QuizHistory> findByUserOrderByTimestampDesc(User user, Pageable pageable);
}
