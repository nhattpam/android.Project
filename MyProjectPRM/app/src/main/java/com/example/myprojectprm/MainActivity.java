package com.example.myprojectprm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}