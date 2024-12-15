package edu.csueb.codepath.fitness_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.parse.ParseUser;

public class workout_summary extends FragmentActivity {

    public String TAG = "workout_summary";
    private TextView workoutType;
    private TextView timeDisplay;
    private TextView caloriesDisplay;
    private Button close;
    public List<String> workouts;
    public double calories;
    private int numOfActivities;
    private Date startTimeDate;
    private Date finishTimeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_summary);

        initializeViews();

        workouts = (List<String>) getIntent().getSerializableExtra("Workout2");
        numOfActivities = workouts.size();

        String workoutTypeString = createWorkoutString(workouts);
        workoutType.setText(workoutTypeString);

        int finalTime = getIntent().getIntExtra("finalTime", 0);
        String time = createTimerString(finalTime);
        timeDisplay.setText(time);

        calories = calculateCalories(finalTime, numOfActivities);
        caloriesDisplay.setText(String.valueOf(calories));

        startTimeDate = (Date) getIntent().getSerializableExtra("startTime");
        finishTimeDate = (Date) getIntent().getSerializableExtra("finishTime");
        Log.e(TAG, "Start Time: " + startTimeDate);
        Log.e(TAG, "Finish Time: " + finishTimeDate);

        close.setOnClickListener(v -> {
            submitWorkout(time, workoutTypeString);
            Intent intent = new Intent(workout_summary.this, MainActivity.class);
            startActivity(intent);
            Animatoo.animateFade(workout_summary.this);
        });
    }

    private void initializeViews() {
        workoutType = findViewById(R.id.tvWorkout_summary);
        timeDisplay = findViewById(R.id.tvWorkoutTimeSummary);
        caloriesDisplay = findViewById(R.id.tvCaloriesDisplay);
        close = findViewById(R.id.btnFinish);
    }

    private void submitWorkout(String time, String workoutTypeString) {
        Workout workout = new Workout();
        workout.setStartDate(startTimeDate);
        workout.setKeyEnd(finishTimeDate);
        workout.setCalories(calories);
        workout.setDuration(time);
        workout.setWorkoutType(workoutTypeString);
        workout.setKeyUser(ParseUser.getCurrentUser());

        workout.saveInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Error while saving workout", e);
            } else {
                Log.i(TAG, "Workout saved successfully");
            }
        });
    }

    public String createWorkoutString(List<String> workouts) {
        StringBuilder workoutTypeString = new StringBuilder();

        for (int i = 0; i < workouts.size(); i++) {
            if (i > 0) {
                workoutTypeString.append(", ");
            }
            workoutTypeString.append(workouts.get(i));
        }

        return workoutTypeString.toString();
    }

    public String createTimerString(int finalTime) {
        int minutes = finalTime / 60;
        int seconds = finalTime % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private double calculateCalories(int finalTime, int numOfActivities) {
        ParseUser user = ParseUser.getCurrentUser();
        Integer weightPounds = user.getInt("weight");

        if (weightPounds == null || weightPounds <= 0) {
            Log.e(TAG, "Invalid weight for user");
            return 0;
        }

        double weightKg = weightPounds / 2.2046;
        double timeHours = finalTime / 60.0;

        int met;
        switch (numOfActivities) {
            case 1:
                met = 4;
                break;
            case 2:
                met = 5;
                break;
            case 3:
                met = 7;
                break;
            case 4:
            case 5:
                met = 8;
                break;
            default:
                met = 0;
                break;
        }

        if (met == 0) {
            Log.e(TAG, "Invalid number of activities");
            return 0;
        }

        double caloriesBurned = timeHours * (met * 3.5 * weightKg) / 200;
        return BigDecimal.valueOf(caloriesBurned).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }
}
