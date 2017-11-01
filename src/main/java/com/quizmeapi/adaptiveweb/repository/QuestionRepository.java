package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findById(int id);

//    @Query(value = "SELECT id, question, courseTopic, choiceA, choiceB, choiceC, choiceD, choiceE, numChoices, answer, level FROM question LIMIT 15", nativeQuery = true)
    Page<Question> findAll(Pageable pageable);
}