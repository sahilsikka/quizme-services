package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.model.UserProficiency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProficiencyRepository extends JpaRepository<UserProficiency, Integer> {
    List<UserProficiency> findAllByUser(User user);
}
