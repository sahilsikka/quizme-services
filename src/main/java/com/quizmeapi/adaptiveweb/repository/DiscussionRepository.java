package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Discussion;
import com.quizmeapi.adaptiveweb.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    Iterable<?> findAllByQuestion(Question questionId);
}
