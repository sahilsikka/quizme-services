package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findById(int id);
    Page<Question> findAll(Pageable pageable);
    List<Question> findByCategoryAndLevels(String category , String level);
    List<Question> findByCategory(String category);

}