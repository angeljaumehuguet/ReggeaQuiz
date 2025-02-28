package com.example.reggeaquiz.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {
    
    @Query("SELECT * FROM questions")
    List<Question> getAllQuestions();
    
    @Query("SELECT * FROM questions ORDER BY RANDOM() LIMIT :limit")
    List<Question> getRandomQuestions(int limit);
    
    @Query("SELECT COUNT(*) FROM questions")
    int getQuestionCount();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);
    
    @Query("DELETE FROM questions")
    void deleteAllQuestions();
}
