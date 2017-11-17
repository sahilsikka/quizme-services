package com.quizmeapi.adaptiveweb.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "topic")
public class Topic {
    @Id
    @GeneratedValue
    private int id;
    private String topicName;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserProficiency> userProficiencies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Set<UserProficiency> getUserProficiencies() {
        return userProficiencies;
    }

    public void setUserProficiencies(Set<UserProficiency> userProficiencies) {
        this.userProficiencies = userProficiencies;
    }
}
