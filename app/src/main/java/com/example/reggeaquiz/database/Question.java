package com.example.reggeaquiz.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String questionText;
    private boolean isTrue;
    private String explanation;
    
    public Question(String questionText, boolean isTrue, String explanation) {
        this.questionText = questionText;
        this.isTrue = isTrue;
        this.explanation = explanation;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public boolean isTrue() {
        return isTrue;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public void setTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
