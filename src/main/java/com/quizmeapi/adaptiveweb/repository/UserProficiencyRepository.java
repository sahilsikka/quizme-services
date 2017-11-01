package com.quizmeapi.adaptiveweb.repository;

import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.model.UserProficiency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProficiencyRepository extends JpaRepository<UserProficiency, Integer> {
    UserProficiency findByUser(User user);
}
