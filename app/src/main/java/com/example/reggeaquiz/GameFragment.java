package com.example.reggeaquiz;

import android.media.MediaPlayer;
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

    // Sonidos de la aplicación
    private MediaPlayer correctSound;
    private MediaPlayer incorrectSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer backgroundMusic;

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

        // Inicializar los sonidos
        initSounds();

        // Set up click listeners
        btnTrue.setOnClickListener(v -> checkAnswer(true));
        btnFalse.setOnClickListener(v -> checkAnswer(false));
        btnNext.setOnClickListener(v -> moveToNextQuestion());

        return view;
    }

    private void initSounds() {
        // Crear los MediaPlayers para cada sonido
        correctSound = MediaPlayer.create(requireContext(), R.raw.correct_answer);
        incorrectSound = MediaPlayer.create(requireContext(), R.raw.wrong_answer);
        gameOverSound = MediaPlayer.create(requireContext(), R.raw.game_over);
        backgroundMusic = MediaPlayer.create(requireContext(), R.raw.background_music);

        // Configurar la música de fondo para que se repita
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadQuestions();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Iniciar música de fondo si está habilitada
        if (preferenceManager.isBackgroundMusicEnabled() && backgroundMusic != null) {
            backgroundMusic.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pausar el temporizador
        if (timer != null) {
            timer.cancel();
        }

        // Pausar la música de fondo
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Liberar los recursos de sonido
        releaseMediaPlayers();
    }

    private void releaseMediaPlayers() {
        if (correctSound != null) {
            correctSound.release();
            correctSound = null;
        }
        if (incorrectSound != null) {
            incorrectSound.release();
            incorrectSound = null;
        }
        if (gameOverSound != null) {
            gameOverSound.release();
            gameOverSound = null;
        }
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
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

        // Reproducir sonido de respuesta incorrecta cuando se acaba el tiempo
        if (preferenceManager.isSoundEffectsEnabled() && incorrectSound != null) {
            incorrectSound.start();
        }
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

            // Reproducir sonido de respuesta correcta
            if (preferenceManager.isSoundEffectsEnabled() && correctSound != null) {
                correctSound.start();
            }
        } else {
            Toast.makeText(requireContext(), R.string.wrong_answer, Toast.LENGTH_SHORT).show();

            // Reproducir sonido de respuesta incorrecta
            if (preferenceManager.isSoundEffectsEnabled() && incorrectSound != null) {
                incorrectSound.start();
            }
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

        // Reproducir sonido de fin de juego
        if (preferenceManager.isSoundEffectsEnabled() && gameOverSound != null) {
            gameOverSound.start();
        }

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