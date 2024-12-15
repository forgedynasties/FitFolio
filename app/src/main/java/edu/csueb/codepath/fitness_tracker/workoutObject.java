package edu.csueb.codepath.fitness_tracker;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseException;

import java.util.Date;

@ParseClassName("Workout")
public class workoutObject extends ParseObject {

    // Default constructor
    public workoutObject() { }

    // Parameterized constructor
    public workoutObject(Date start, Date end, int calories, String duration, String workoutType) {
        setStart(start);
        setEnd(end);
        setCalories(calories);
        setDuration(duration);
        setWorkoutType(workoutType);
    }

    // Setters
    public void setStart(Date start) {
        put("start", start);
    }

    public void setEnd(Date end) {
        put("end", end);
    }

    public void setCalories(int calories) {
        put("calories", calories);
    }

    public void setDuration(String duration) {
        put("duration", duration);
    }

    public void setWorkoutType(String workoutType) {
        put("workoutType", workoutType);
    }

    // Getters
    public Date getStart() {
        return getDate("start");
    }

    public Date getEnd() {
        return getDate("end");
    }

    public int getCalories() {
        return getInt("calories");
    }

    public String getDuration() {
        return getString("duration");
    }

    public String getWorkoutType() {
        return getString("workoutType");
    }

    // toString for debugging/logging
    @Override
    public String toString() {
        return "WorkoutObject{" +
                "start=" + getStart() +
                ", end=" + getEnd() +
                ", calories=" + getCalories() +
                ", duration='" + getDuration() + '\'' +
                ", workoutType='" + getWorkoutType() + '\'' +
                '}';
    }
}
