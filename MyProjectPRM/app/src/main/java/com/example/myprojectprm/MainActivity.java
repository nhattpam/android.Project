package com.example.myprojectprm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameContainer;

    private ProductFragment productFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;
    private ProfileFragment profileFragment;

    private String loggedInUsername; // Variable to store the username of the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS_NAME, MODE_PRIVATE);
        loggedInUsername = sharedPreferences.getString("username", "");

        // Get the logged-in username from the intent
//        loggedInUsername = getIntent().getStringExtra("username");
        Log.d("main", loggedInUsername);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameContainer = findViewById(R.id.frame_container);

        // Initialize the fragments
        productFragment = new ProductFragment();
        mapFragment = new MapFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        // Set the default fragment
        setFragment(productFragment);

        // Handle item selection events
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_product:
                        setFragment(productFragment);
                        return true;
                    case R.id.menu_map:
                        setFragment(mapFragment);
                        return true;
                    case R.id.menu_chat:
                        setFragment(chatFragment);
                        return true;
                    case R.id.menu_profile:
                        setFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("username", loggedInUsername);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    //cart icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return true;
    }

    //OPEN cart Screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            // Open the cart activity or perform any action you desire
            openCartFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void openCartFragment() {
        // Create a new instance of the CartFragment
        CartFragment cartFragment = new CartFragment();

        // Pass the username to the cart
        Bundle bundle = new Bundle();
        bundle.putString("username", loggedInUsername);
        cartFragment.setArguments(bundle);

        // Replace the current fragment with the CartFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, cartFragment)
                .addToBackStack(null) // Add the fragment to the back stack to enable back navigation
                .commit();
    }

}