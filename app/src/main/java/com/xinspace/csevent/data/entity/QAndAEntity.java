package com.xinspace.csevent.data.entity;

import java.io.Serializable;

/**
 * 登录实体
 */
public class QAndAEntity implements Serializable{
    private String id;//问答id
    private String questions;//问题
    private String answers;//答案

    public QAndAEntity(String id, String questions, String answers) {
        this.id = id;
        this.questions = questions;
        this.answers = answers;
    }

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

    @Override
    public String toString() {
        return "QAndAEntity{" +
                "id='" + id + '\'' +
                ", questions='" + questions + '\'' +
                ", answers='" + answers + '\'' +
                '}';
    }
}
