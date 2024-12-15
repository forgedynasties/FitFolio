package edu.csueb.codepath.fitness_tracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class workout_timer extends FragmentActivity {

    private static final String TAG = "WorkoutTimer";
    private Chronometer chronometer;
    private boolean isRunning;
    private boolean isStarted;
    private Button btnStartStop;
    private Button btnReset;
    private Button btnFinishWorkout;
    private long pauseOffset;
    private int elapsedTimeSeconds;
    private List<String> workouts;
    private TextView workoutTitle;
    private Date startTime;
    private Date finishTime;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_timer);

        initializeViews();
        setupWorkoutList();

        btnStartStop.setOnClickListener(v -> toggleTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btnFinishWorkout.setOnClickListener(v -> finishWorkout());

        chronometer.setOnChronometerTickListener(this::updateProgressBar);
    }

    private void initializeViews() {
        chronometer = findViewById(R.id.timer);
        btnStartStop = findViewById(R.id.btnStart_stop);
        btnReset = findViewById(R.id.btnReset);
        btnFinishWorkout = findViewById(R.id.btnFinish);
        workoutTitle = findViewById(R.id.tvWorkoutList);
        progressBar = findViewById(R.id.circleProgress);

        isRunning = false;
        isStarted = false;
    }

    private void setupWorkoutList() {
        workouts = (List<String>) getIntent().getSerializableExtra("Workout");
        if (workouts != null) {
            String workoutDisplayText = String.join(", ", workouts);
            workoutTitle.setText(workoutDisplayText);
        } else {
            Log.e(TAG, "Workout list is null");
        }
    }

    private void toggleTimer() {
        if (!isStarted) {
            startTimer();
        } else if (isRunning) {
            pauseTimer();
        } else {
            resumeTimer();
        }
    }

    private void startTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        isRunning = true;
        isStarted = true;
        updateStartStopButtonState(true);
        startTime = new Date();
        Log.i(TAG, "Workout started at: " + startTime);
    }

    private void pauseTimer() {
        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        isRunning = false;
        elapsedTimeSeconds = (int) (pauseOffset / 1000);
        updateStartStopButtonState(false);
        Log.i(TAG, "Workout paused. Elapsed time: " + elapsedTimeSeconds + " seconds");
    }

    private void resumeTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        isRunning = true;
        updateStartStopButtonState(true);
        Log.i(TAG, "Workout resumed");
    }

    private void updateStartStopButtonState(boolean isRunning) {
        btnStartStop.setText(isRunning ? "Stop" : "Start");
        btnStartStop.setBackgroundTintList(getResources().getColorStateList(
                isRunning ? R.color.pastelred : R.color.pastelgreen
        ));
    }

    private void resetTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        isStarted = false;
        isRunning = false;
        progressBar.setProgress(0);
        btnStartStop.setText("Start");
        btnStartStop.setBackgroundTintList(getResources().getColorStateList(R.color.pastelgreen));
        Toast.makeText(this, "Timer reset", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Timer reset");
    }

    private void updateProgressBar(Chronometer chronometer) {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int seconds = (int) (elapsedMillis / 1000) % 60;
        int progress = Math.round(seconds * 1.666f); // Convert to percentage
        progressBar.setProgress(progress);
        Log.d(TAG, "Progress updated to: " + progress + "%");
    }

    private void finishWorkout() {
        if (!isStarted) {
            Toast.makeText(this, "Workout not started!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isRunning) {
            elapsedTimeSeconds = (int) (pauseOffset / 1000);
        } else {
            long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
            elapsedTimeSeconds = (int) (elapsedMillis / 1000);
        }

        finishTime = new Date();
        Log.i(TAG, "Workout finished at: " + finishTime);

        Intent intent = new Intent(this, workout_summary.class);
        intent.putExtra("WorkoutList", (Serializable) workouts);
        intent.putExtra("ElapsedTime", elapsedTimeSeconds);
        intent.putExtra("StartTime", startTime);
        intent.putExtra("FinishTime", finishTime);
        startActivity(intent);
        Animatoo.animateSlideLeft(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
}
