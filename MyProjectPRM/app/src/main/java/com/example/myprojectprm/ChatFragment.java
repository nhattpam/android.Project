package com.example.myprojectprm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChatFragment extends Fragment {
    private String loggedInUsername; // Variable to store the username of the logged-in user
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // Retrieve the passed username from the arguments
//        loggedInUsername = getArguments().getString("username");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppConstants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", "");
        Log.d("chat", loggedInUsername);


        return view;
    }
}