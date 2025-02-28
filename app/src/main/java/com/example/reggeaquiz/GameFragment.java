package com.example.reggeaquiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reggeaquiz.database.Question;
import com.example.reggeaquiz.database.QuestionDatabase;
import com.example.reggeaquiz.utils.PreferenceManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameFragment extends Fragment {

    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvTimer;
    private TextView tvExplanation;
    private Button btnTrue;
    private Button btnFalse;
    private Button btnNext;
    
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean questionAnswered = false;
    
    private PreferenceManager preferenceManager;
    private QuestionDatabase database;
    private ExecutorService executorService;
    
    private CountDownTimer timer;
    private static final int COUNTDOWN_TIME = 20; // seconds per question
    private int timeRemaining = COUNTDOWN_TIME;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        
        // Initialize views
        tvQuestion = view.findViewById(R.id.tv_question);
        tvScore = view.findViewById(R.id.tv_score);
        tvTimer = view.findViewById(R.id.tv_timer);
        tvExplanation = view.findViewById(R.id.tv_explanation);
        btnTrue = view.findViewById(R.id.btn_true);
        btnFalse = view.findViewById(R.id.btn_false);
        btnNext = view.findViewById(R.id.btn_next);
        
        // Initialize preferences and database
        preferenceManager = new PreferenceManager(requireContext());
        database = QuestionDatabase.getDatabase(requireContext());
        executorService = Executors.newSingleThreadExecutor();
        
        // Set up click listeners
        btnTrue.setOnClickListener(v -> checkAnswer(true));
        btnFalse.setOnClickListener(v -> checkAnswer(false));
        btnNext.setOnClickListener(v -> moveToNextQuestion());
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadQuestions();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }
    
    private void loadQuestions() {
        executorService.execute(() -> {
            int questionsPerRound = preferenceManager.getQuestionsPerRound();
            questionList = database.questionDao().getRandomQuestions(questionsPerRound);
            requireActivity().runOnUiThread(this::startGame);
        });
    }
    
    private void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        updateScore();
        if (!questionList.isEmpty()) {
            showQuestion(questionList.get(currentQuestionIndex));
        } else {
            tvQuestion.setText("Error: No se pudieron cargar las preguntas");
            disableAnswerButtons();
        }
    }
    
    private void showQuestion(Question question) {
        tvQuestion.setText(question.getQuestionText());
        tvExplanation.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        enableAnswerButtons();
        questionAnswered = false;
        startTimer();
    }
    
    private void startTimer() {
        timeRemaining = COUNTDOWN_TIME;
        updateTimerText();
        
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new CountDownTimer(COUNTDOWN_TIME * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = (int) (millisUntilFinished / 1000);
                updateTimerText();
            }
            
            @Override
            public void onFinish() {
                timeRemaining = 0;
                updateTimerText();
                if (!questionAnswered) {
                    timeUp();
                }
            }
        }.start();
    }
    
    private void timeUp() {
        questionAnswered = true;
        disableAnswerButtons();
        showCorrectAnswer();
        btnNext.setVisibility(View.VISIBLE);
    }
    
    private void updateTimerText() {
        tvTimer.setText(getString(R.string.time_remaining, timeRemaining));
    }
    
    private void checkAnswer(boolean userAnswer) {
        if (questionAnswered) return;
        
        questionAnswered = true;
        if (timer != null) {
            timer.cancel();
        }
        
        Question currentQuestion = questionList.get(currentQuestionIndex);
        boolean isCorrect = (userAnswer == currentQuestion.isTrue());
        
        if (isCorrect) {
            score++;
            updateScore();
            Toast.makeText(requireContext(), R.string.correct_answer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }
        
        showCorrectAnswer();
        disableAnswerButtons();
        btnNext.setVisibility(View.VISIBLE);
    }
    
    private void showCorrectAnswer() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        tvExplanation.setText(currentQuestion.getExplanation());
        tvExplanation.setVisibility(View.VISIBLE);
    }
    
    private void updateScore() {
        tvScore.setText(getString(R.string.game_score, score));
    }
    
    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            showQuestion(questionList.get(currentQuestionIndex));
        } else {
            endGame();
        }
    }
    
    private void endGame() {
        preferenceManager.saveMaxScore(score);
        
        // Show final score
        String finalScore = getString(R.string.final_score, score, questionList.size());
        tvQuestion.setText(R.string.game_over);
        tvExplanation.setText(finalScore);
        tvExplanation.setVisibility(View.VISIBLE);
        
        // Change "Next" button to "Play Again"
        btnNext.setText(R.string.btn_play_again);
        btnNext.setOnClickListener(v -> restartGame());
    }
    
    private void restartGame() {
        btnNext.setText(R.string.btn_next);
        btnNext.setOnClickListener(v -> moveToNextQuestion());
        loadQuestions();
    }
    
    private void enableAnswerButtons() {
        btnTrue.setEnabled(true);
        btnFalse.setEnabled(true);
    }
    
    private void disableAnswerButtons() {
        btnTrue.setEnabled(false);
        btnFalse.setEnabled(false);
    }
}
