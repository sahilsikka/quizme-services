package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Discussion;
import com.quizmeapi.adaptiveweb.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    Iterable<Discussion> findAllByQuestion(Question question);

    List<Discussion> findAll();
}
