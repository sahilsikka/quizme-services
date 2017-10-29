package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.quizmeapi.adaptiveweb.model.Discussion;
import com.quizmeapi.adaptiveweb.model.Question;
import com.quizmeapi.adaptiveweb.model.User;
import com.quizmeapi.adaptiveweb.repository.DiscussionRepository;
import com.quizmeapi.adaptiveweb.repository.QuestionRepository;
import com.quizmeapi.adaptiveweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/discussion", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscussionController {

    private final DiscussionRepository discussionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Autowired
    public DiscussionController(DiscussionRepository discussionRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.discussionRepository = discussionRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @CrossOrigin
    @ResponseBody
    public List<Discussion> getAllPosts() {
        return discussionRepository.findAll();
    }

    @RequestMapping(value = "/{question_id}", method = RequestMethod.GET)
    @CrossOrigin
    @ResponseBody
    public Iterable<Discussion> getAllPostsByQuestionId(@PathVariable("question_id") Integer questionId) {
        Question question = questionRepository.findById(questionId);
        return discussionRepository.findAllByQuestion(question);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @CrossOrigin
    @ResponseBody
    public ResponseEntity<?> postToDiscussionBoard(@RequestBody JsonNode jsonNode) {
        try {
            Discussion discussion = new Discussion();
            if (!jsonNode.has("question_id") && !jsonNode.has("user_id")) {
                return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
            } else {
                User user = userRepository.findById(jsonNode.get("user_id").asInt());
                Question question = questionRepository.findById(jsonNode.get("question_id").asInt());
                discussion.setPost(String.valueOf(jsonNode.get("post").asText()));
                discussion.setDownVote(jsonNode.get("up_vote").asInt());
                discussion.setDownVote(jsonNode.get("down_vote").asInt());
                discussion.setUser(user);
                discussion.setQuestion(question);
                discussionRepository.save(discussion);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot parse request");
        }
    }
}
