package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.UserProficiencyRepository;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;
    private final UserProficiencyRepository userProficiencyRepository;
    private ObjectMapper objectMapper;

    @Autowired
    UserController(UserRepository userRepository, UserProficiencyRepository userProficiencyRepository) {
        this.userRepository = userRepository;
        this.userProficiencyRepository = userProficiencyRepository;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public User getUser(@PathVariable("id") Integer id) {
        return this.userRepository.findById(id);
    }

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public ObjectNode checkLogin(@RequestHeader(value = "email", required = true) String email,
                                 @RequestHeader(value = "password", required = true) String password)
            throws JsonProcessingException {
        User user;
        try {
            user = this.userRepository.findByEmailAndPassword(email, password);
            ObjectNode[] objectNode = new ObjectNode[1];
            objectNode[0] = objectMapper.createObjectNode();
            if (user != null) {
                objectNode[0].put("id", user.getId());
                objectNode[0].put("email", user.getEmail());
                objectNode[0].put("fname", user.getFname());
                objectNode[0].put("lname", user.getLname());
                objectNode[0].put("status", "Success");
            } else {
                objectNode[0].put("status", "Login Failed");
            }
            return objectNode[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> postUserDetails(@RequestBody String input) {
        User user;
        User emailRecord;
        ObjectNode[] objectNode = new ObjectNode[1];
        objectNode[0] = objectMapper.createObjectNode();
        try {
            user = objectMapper.readValue(input, User.class);
            String email = user.getEmail();
            String password = user.getPassword();
            if (email == null && password == null) {
                objectNode[0].put("status", "Email id and password required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectNode[0]);
            }
            emailRecord = userRepository.findByEmail(email);
            if (emailRecord != null) {
                objectNode[0].put("status", "Email id exists");
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(objectNode[0]);
            } else {
//                List<Topic> topics = topicRepository.findAll();
//                for (Topic topic: topics) {
//                    UserProficiency userProficiency = new UserProficiency();
//                    userProficiency.setTopic(topic);
//                    userProficiency.setSkillTopic(topic.getTopicName());
//                    userProficiency.setUser(user);
//                    userProficiency.setProficiency(1);
//                    userProficiencyRepository.save(userProficiency);
//                }
                userRepository.save(user);
                objectNode[0].put("status", "Success");
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(objectNode[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            objectNode[0].put("status", "Error!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(objectNode[0]);
        }
    }

    @RequestMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateUserDetails(@RequestBody String input, @PathVariable("id") Integer id) throws IOException {
        try {
            User newUser;
            User user = this.userRepository.findById(id);
            if (user != null) {
                newUser = this.objectMapper.readValue(input, User.class);
                newUser.setId(id);
                newUser.setEmail(user.getEmail());
                newUser.setPassword(user.getPassword());
                if (user.getFname() != null) newUser.setFname(user.getFname());
                if (user.getLname() != null) newUser.setLname(user.getLname());
                if (user.getAge() != null) newUser.setAge(user.getAge());
                if (user.getCountry() != null) newUser.setCountry(user.getCountry());
                if (user.getOrganization() != null) newUser.setOrganization(user.getOrganization());
                if (user.getProfile_pic() != null) newUser.setProfile_pic(user.getProfile_pic());
                if (user.getGender() != null) newUser.setGender(user.getGender());
                this.userRepository.save(newUser);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(input);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to parse request");
        }
    }
}