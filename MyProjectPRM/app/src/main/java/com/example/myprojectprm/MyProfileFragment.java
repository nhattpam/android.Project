package com.example.myprojectprm;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyProfileFragment extends Fragment {

    private EditText editTextUsername, editTextPassword, editTextFullName, editTextAddress, editTextPhone;
    private Button buttonUpdateProfile;

    private String loggedInUsername;

    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullName";
    private static final String COLUMN_ADDRESS = "address";

    private static final String COLUMN_PHONE = "phone";

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", "");
        Log.d("profileMY: ", loggedInUsername);

        // Inflate the fragment_my_profile.xml layout
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        // Initialize the EditText fields and populate the user profile
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        buttonUpdateProfile = view.findViewById(R.id.btn_updateProfile);

        // Call the populateUserProfile method with the logged-in username
        populateUserProfile(loggedInUsername);

        // Set the click listener for the update button
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String fullName = editTextFullName.getText().toString();
                String address = editTextAddress.getText().toString();
                String phone = editTextPhone.getText().toString();

                // Call a method to update the user's profile using the DatabaseHelper
                updateProfile(username, password, fullName, address, phone);
            }
        });

        return view;
    }
    // Method to update the user's profile using the DatabaseHelper
    private void updateProfile(String username, String password, String fullName, String address, String phone) {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        // Update the user's profile in the database
        boolean isUpdated = dbHelper.updateUser(username, password, fullName, address, phone);

        // Check if the profile update was successful
        if (isUpdated) {
            // If the profile was successfully updated, show a toast message
            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // If the profile update failed, show an error message
            Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUserProfile(String username) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        ContentValues profileData = dbHelper.populateUserProfile(username);

        if (profileData != null) {
            String fetchedUsername = profileData.getAsString(COLUMN_USERNAME);
            String password = profileData.getAsString(COLUMN_PASSWORD);
            String fullName = profileData.getAsString(COLUMN_FULLNAME);
            String address = profileData.getAsString(COLUMN_ADDRESS);
            String phone = profileData.getAsString(COLUMN_PHONE);

            editTextUsername.setText(fetchedUsername);
            editTextPassword.setText(password);
            editTextFullName.setText(fullName);
            editTextAddress.setText(address);
            editTextPhone.setText(phone);
        }
    }

}
