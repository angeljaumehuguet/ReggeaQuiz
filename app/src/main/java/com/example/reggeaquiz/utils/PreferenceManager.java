package com.example.reggeaquiz.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    
    private static final String PREF_NAME = "ReggeaQuizPrefs";
    private static final String KEY_MAX_SCORE = "max_score";
    private static final String KEY_QUESTIONS_PER_ROUND = "questions_per_round";
    private static final String KEY_SOUND_EFFECTS = "sound_effects";
    private static final String KEY_BACKGROUND_MUSIC = "background_music";
    
    private static final int DEFAULT_QUESTIONS_PER_ROUND = 5;
    private static final boolean DEFAULT_SOUND_EFFECTS = true;
    private static final boolean DEFAULT_BACKGROUND_MUSIC = true;
    
    private final SharedPreferences sharedPreferences;
    
    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveMaxScore(int score) {
        int currentMax = getMaxScore();
        if (score > currentMax) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_MAX_SCORE, score);
            editor.apply();
        }
    }
    
    public int getMaxScore() {
        return sharedPreferences.getInt(KEY_MAX_SCORE, 0);
    }
    
    public void resetMaxScore() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_MAX_SCORE, 0);
        editor.apply();
    }
    
    public void setQuestionsPerRound(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_QUESTIONS_PER_ROUND, count);
        editor.apply();
    }
    
    public int getQuestionsPerRound() {
        return sharedPreferences.getInt(KEY_QUESTIONS_PER_ROUND, DEFAULT_QUESTIONS_PER_ROUND);
    }
    
    public void setSoundEffectsEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SOUND_EFFECTS, enabled);
        editor.apply();
    }
    
    public boolean isSoundEffectsEnabled() {
        return sharedPreferences.getBoolean(KEY_SOUND_EFFECTS, DEFAULT_SOUND_EFFECTS);
    }
    
    public void setBackgroundMusicEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_BACKGROUND_MUSIC, enabled);
        editor.apply();
    }
    
    public boolean isBackgroundMusicEnabled() {
        return sharedPreferences.getBoolean(KEY_BACKGROUND_MUSIC, DEFAULT_BACKGROUND_MUSIC);
    }
}
