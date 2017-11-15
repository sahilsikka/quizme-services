package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.model.UserProficiency;
import com.quizmeapi.adaptiveweb.repository.UserProficiencyRepository;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/userProficiency")
public class UserProficiencyController {

    private final UserProficiencyRepository userProficiencyRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProficiencyController(UserProficiencyRepository userProficiencyRepository, UserRepository userRepository) {
        this.userProficiencyRepository = userProficiencyRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    public List<UserProficiency> getProficiencyByUserId(@PathVariable("user_id") int userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return null;
        }
        List<UserProficiency> userProficiencies = userProficiencyRepository.findAllByUser(user);
        if (userProficiencies == null) {
            return null;
        }
        return userProficiencies;
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.PUT)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> updateUserProficiency(@PathVariable("user_id") int userId, @RequestBody JsonNode rawInput) {
        try {
            User user = userRepository.findById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User id not found");
            }
            if (!rawInput.has("skill_topic") && !rawInput.has("proficiency")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("skill_topic and proficiency needed");
            }
            UserProficiency userProficiency;
            UserProficiency oldProf = userProficiencyRepository.findByUser(user);
            if (oldProf == null) {
                userProficiency = new UserProficiency();
            } else {
                userProficiency = oldProf;
            }
            int prof = rawInput.get("proficiency").asInt();
            if (prof < 0 || prof > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Proficiency value must be 0-5");
            }
            String skillTopic = rawInput.get("skill_topic").asText();
            userProficiency.setUser(user);
            userProficiency.setSkillTopic(skillTopic);
            userProficiency.setProficiency(prof);
            userProficiencyRepository.save(userProficiency);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request cannot be parsed");
        }
    }
}
