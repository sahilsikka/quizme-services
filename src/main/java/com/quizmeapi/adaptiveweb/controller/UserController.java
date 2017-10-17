package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
public class UserController {
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    @Autowired
    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/login")
    @CrossOrigin
    @ResponseBody
    String checkLogin(@RequestHeader(value = "email", required = true) String email, @RequestHeader(value = "password", required = true) String password) throws JsonProcessingException {
        User user = null;
        try {
            System.out.println("Email: "+email+ " and password: "+password);
            user = this.userRepository.findByEmailAndPassword(email, password);
            if (user == null) {
                return this.objectMapper.writeValueAsString("Login Failed");
            }
            return this.objectMapper.writeValueAsString("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return this.objectMapper.writeValueAsString("Unable to parse request");
        }
    }

    @PostMapping("/registration")
    @CrossOrigin
    @ResponseBody
    ResponseEntity<?> postUserDetails(@RequestBody String input) {
        User user = null;
        User emailRecord = null;
        try {
            user = objectMapper.readValue(input, User.class);
            String email = user.getEmail();
            String password = user.getPassword();
            if (email == null && password == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email id and password required");
            }
            emailRecord = userRepository.findByEmail(email);
            if (emailRecord != null) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Email Id Exists");
            } else {
                this.userRepository.save(user);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/"+user.getId())
                        .buildAndExpand(user.getId()).toUri();
                return ResponseEntity.created(location).body(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to parse request");
        }
    }

}