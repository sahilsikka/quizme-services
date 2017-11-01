package com.quizmeapi.adaptiveweb.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    private String gender;
    private String fname;
    private String lname;
    private String profile_pic;
    private Integer age;
    private String country;
    private String organization;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Discussion> discussions;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuizHistory> quizHistories;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuizSession> quizSessions;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserProficiency> userProficiencies;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Set<Discussion> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(Set<Discussion> discussions) {
        this.discussions = discussions;
    }

    public Set<QuizHistory> getQuizHistories() {
        return quizHistories;
    }

    public void setQuizHistories(Set<QuizHistory> quizHistories) {
        this.quizHistories = quizHistories;
    }

    public Set<QuizSession> getQuizSessions() {
        return quizSessions;
    }

    public void setQuizSessions(Set<QuizSession> quizSessions) {
        this.quizSessions = quizSessions;
    }

    public Set<UserProficiency> getUserProficiencies() {
        return userProficiencies;
    }

    public void setUserProficiencies(Set<UserProficiency> userProficiencies) {
        this.userProficiencies = userProficiencies;
    }
}
