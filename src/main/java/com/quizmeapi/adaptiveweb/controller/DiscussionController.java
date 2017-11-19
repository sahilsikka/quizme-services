package com.quizmeapi.adaptiveweb.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/discussion", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class DiscussionController {

    private final DiscussionRepository discussionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public DiscussionController(DiscussionRepository discussionRepository, QuestionRepository questionRepository, UserRepository userRepository) {
        this.discussionRepository = discussionRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Discussion> getAllPosts() {
        return discussionRepository.findAll();
    }

    @RequestMapping(value = "/{question_id}", method = RequestMethod.GET)
    @ResponseBody
    public List<ObjectNode> getAllPostsByQuestionId(@PathVariable("question_id") Integer questionId) {
        Question question = questionRepository.findById(questionId);
        List<Discussion> questions = (List<Discussion>) discussionRepository.findAllByQuestion(question);
        List<ObjectNode> ans = new ArrayList<>();
        for (Discussion post: questions) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("id", post.getId());
            objectNode.put("email", post.getUser().getEmail());
            objectNode.put("post", post.getPost());
            objectNode.put("upvote", post.getUpVote());
            objectNode.put("downvote", post.getDownVote());
            objectNode.put("timestamp", String.valueOf(post.getTimestamp()));
            ans.add(objectNode);
        }
        return ans;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
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
                discussion.setUpVote(jsonNode.get("up_vote").asInt());
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

    @RequestMapping(value = "/upvote/{post_id}", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectNode putUpVote(@PathVariable("post_id") int postId) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            Discussion discussion = discussionRepository.findById(postId);
            int vote;
            if (discussion.getUpVote() != null) {
                vote = discussion.getUpVote();
            } else {
                vote = 0;
            }
            discussion.setUpVote(vote + 1);
            discussionRepository.save(discussion);
            objectNode.put("vote", vote + 1);
            objectNode.put("status", "Success");
            return objectNode;
        } catch (Exception e) {
            System.out.println(e);
            objectNode.put("status", "No such post id found");
            return objectNode;
        }
    }

    @RequestMapping(value = "/downvote/{post_id}", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectNode putDownVote(@PathVariable("post_id") int postId) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        try {
            Discussion discussion = discussionRepository.findById(postId);
            int vote;
            if (discussion.getDownVote() != null) {
                vote = discussion.getDownVote();
            } else {
                vote = 0;
            }
            discussion.setDownVote(vote + 1);
            discussionRepository.save(discussion);
            objectNode.put("vote", vote + 1);
            objectNode.put("status", "Success");
            return objectNode;
        } catch (Exception e) {
            System.out.println(e);
            objectNode.put("status", "No such post id found");
            return objectNode;
        }
    }
}
