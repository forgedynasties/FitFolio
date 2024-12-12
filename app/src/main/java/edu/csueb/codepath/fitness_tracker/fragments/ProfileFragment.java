package edu.csueb.codepath.fitness_tracker.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import edu.csueb.codepath.fitness_tracker.LoginActivity;
import edu.csueb.codepath.fitness_tracker.ProfileEdit;
import edu.csueb.codepath.fitness_tracker.R;
import edu.csueb.codepath.fitness_tracker.SignupActivity;
public class ProfileFragment extends Fragment {
    TextView tvName;
    TextView tvUsername;
    TextView tvUserHeight;
    TextView tvUserWeight;
    RecyclerView rvWorkouts;
    ImageView ivProfileImage;
    ImageButton btnLogout;
    ImageButton btnEdit;
    String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }
    // The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here. E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvUsernameProf);
        tvUsername = view.findViewById(R.id.tvName);
        tvUserHeight = view.findViewById(R.id.tvUserHeight);
        tvUserWeight = view.findViewById(R.id.tvUserWeight);
        rvWorkouts = view.findViewById(R.id.rvWorkouts);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);
        getCurrentUser();
        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        // Edit Profile
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileEdit.class);
                startActivity(i);
            }
        });
    }
    public void getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            // Redirect to the login screen if the user is not logged in
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return; // Exit the method early
        }

        Log.i(TAG, "@" + currentUser.getUsername());
        tvName.setText("@" + currentUser.getUsername());
        tvUsername.setText(currentUser.getString("firstname") + " " + currentUser.getString("lastname"));
        tvUserHeight.setText(String.valueOf(currentUser.get("height")));
        tvUserWeight.setText(String.valueOf(currentUser.get("weight")));

        ParseFile profileImageFile = currentUser.getParseFile("profile_image");
        if (profileImageFile != null) {
            profileImageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Log.i(TAG, "Data received: " + profileImageFile.getUrl());
                        Glide.with(getContext())
                                .load(profileImageFile.getUrl())
                                .centerCrop()
                                .circleCrop()
                                .into(ivProfileImage);
                    } else {
                        Log.e(TAG, "Error occurred, data not retrieved", e);
                    }
                }
            });
        } else {
            Log.e(TAG, "Profile image file is null");
        }
    }
}