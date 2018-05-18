package com.xinspace.csevent.login.model;

import java.io.Serializable;

/**
 * Created by Android on 2016/11/10.
 */
public class HotQuestionBean implements Serializable{

    private String id;
    private String questions;
    private String answers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
