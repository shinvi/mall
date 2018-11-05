package com.shinvi.mall.pojo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserDo {
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String phone;

    @JsonIgnore
    private String pwQuestion;
    @JsonIgnore
    private String pwAnswer;

    private Integer role;

    public UserDo(Integer id, String username, String password, String email, String phone, String pwQuestion, String pwAnswer, Integer role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.pwQuestion = pwQuestion;
        this.pwAnswer = pwAnswer;
        this.role = role;
    }

    public UserDo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPwQuestion() {
        return pwQuestion;
    }

    public void setPwQuestion(String pwQuestion) {
        this.pwQuestion = pwQuestion == null ? null : pwQuestion.trim();
    }

    public String getPwAnswer() {
        return pwAnswer;
    }

    public void setPwAnswer(String pwAnswer) {
        this.pwAnswer = pwAnswer == null ? null : pwAnswer.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}