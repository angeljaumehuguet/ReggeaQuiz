package com.example.reggeaquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reggeaquiz.utils.PreferenceManager;

public class SettingsFragment extends Fragment {
    
    private TextView tvMaxScore;
    private TextView tvQuestionsPerRound;
    private SeekBar seekBarQuestions;
    private Button btnResetScore;
    private Switch switchSoundEffects;
    private Switch switchBackgroundMusic;
    
    private PreferenceManager preferenceManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        // Initialize preferences manager
        preferenceManager = new PreferenceManager(requireContext());
        
        // Initialize views
        tvMaxScore = view.findViewById(R.id.tv_max_score);
        tvQuestionsPerRound = view.findViewById(R.id.tv_questions_per_round);
        seekBarQuestions = view.findViewById(R.id.seek_bar_questions);
        btnResetScore = view.findViewById(R.id.btn_reset_score);
        switchSoundEffects = view.findViewById(R.id.switch_sound_effects);
        switchBackgroundMusic = view.findViewById(R.id.switch_background_music);
        
        // Update UI with current settings
        updateUI();
        
        // Setup listeners
        btnResetScore.setOnClickListener(v -> resetMaxScore());
        
        seekBarQuestions.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ensure minimum of 5 questions
                int questions = progress + 5;
                tvQuestionsPerRound.setText(String.valueOf(questions));
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int questions = seekBar.getProgress() + 5;
                preferenceManager.setQuestionsPerRound(questions);
            }
        });
        
        switchSoundEffects.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setSoundEffectsEnabled(isChecked);
        });
        
        switchBackgroundMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setBackgroundMusicEnabled(isChecked);
        });
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    
    private void updateUI() {
        int maxScore = preferenceManager.getMaxScore();
        tvMaxScore.setText(getString(R.string.max_score, maxScore));
        
        int questionsPerRound = preferenceManager.getQuestionsPerRound();
        tvQuestionsPerRound.setText(String.valueOf(questionsPerRound));
        seekBarQuestions.setProgress(questionsPerRound - 5); // Adjust for minimum of 5
        
        switchSoundEffects.setChecked(preferenceManager.isSoundEffectsEnabled());
        switchBackgroundMusic.setChecked(preferenceManager.isBackgroundMusicEnabled());
    }
    
    private void resetMaxScore() {
        preferenceManager.resetMaxScore();
        updateUI();
        Toast.makeText(requireContext(), R.string.settings_reset_success, Toast.LENGTH_SHORT).show();
    }
}
