package com.example.reggeaquiz.utils;

public class Constants {
    
    // Fragment Tags
    public static final String TAG_GAME_FRAGMENT = "game_fragment";
    public static final String TAG_INFO_FRAGMENT = "info_fragment";
    public static final String TAG_SETTINGS_FRAGMENT = "settings_fragment";
    
    // Game settings
    public static final int DEFAULT_QUESTIONS_PER_ROUND = 5;
    public static final int MIN_QUESTIONS_PER_ROUND = 5;
    public static final int MAX_QUESTIONS_PER_ROUND = 20;
    public static final int DEFAULT_TIMER_SECONDS = 20;
    
    // Preference keys
    public static final String PREF_NAME = "ReggeaQuizPrefs";
    public static final String KEY_MAX_SCORE = "max_score";
    public static final String KEY_QUESTIONS_PER_ROUND = "questions_per_round";
    public static final String KEY_SOUND_EFFECTS = "sound_effects";
    public static final String KEY_BACKGROUND_MUSIC = "background_music";
    
    // Animation durations
    public static final int ANIMATION_DURATION_SHORT = 500;
    public static final int ANIMATION_DURATION_MEDIUM = 1000;
    public static final int ANIMATION_DURATION_LONG = 2000;
}
