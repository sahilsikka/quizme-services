package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Topic findTopicByTopicName(String topicName);
}
