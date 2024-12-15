package edu.csueb.codepath.fitness_tracker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.csueb.codepath.fitness_tracker.fragments.TrackFragment;

public class WorkoutListAdapter extends RecyclerView.Adapter<WorkoutListAdapter.ViewHolder> {
//
    public interface OnClickListener {
        void onItemClicked(int position);
    }

    private List<String> workouts = new ArrayList<>();
    private final List<String> checkedWorkouts = new ArrayList<>();
    private final TrackFragment trackFragment;
    private OnClickListener clickListener;

    // Constructor
    public WorkoutListAdapter(List<String> workoutList, TrackFragment trackFragment) {
        this.workouts = workoutList != null ? workoutList : new ArrayList<>();
        this.trackFragment = trackFragment;
    }

    // Setter for click listener
    public void setOnClickListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workout_task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String workout = workouts.get(position);
        holder.bind(workout, position);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    // Update workout list
    public void setWorkouts(List<String> workoutList) {
        this.workouts = workoutList != null ? workoutList : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Get checked workouts
    public List<String> getCheckedWorkouts() {
        Log.i("WorkoutListAdapter", "Checked workouts: " + checkedWorkouts);
        return new ArrayList<>(checkedWorkouts);
    }

    // ViewHolder class
    class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void bind(String workout, int position) {
            checkBox.setText(workout);
            checkBox.setChecked(checkedWorkouts.contains(workout));

            // Handle checkbox click
            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    if (!checkedWorkouts.contains(workout)) {
                        checkedWorkouts.add(workout);
                        Log.i("WorkoutListAdapter", "Checked: " + workout);
                    }
                } else {
                    checkedWorkouts.remove(workout);
                    Log.i("WorkoutListAdapter", "Unchecked: " + workout);
                }

                // Notify listener if set
                if (clickListener != null) {
                    clickListener.onItemClicked(position);
                }
            });
        }
    }
}
