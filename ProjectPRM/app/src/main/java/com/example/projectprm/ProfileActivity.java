package com.example.projectprm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_product:
                        // Start the ProductActivity
                        startActivity(new Intent(ProfileActivity.this, ProductActivity.class));
                        finish();
                        return true;
                    case R.id.menu_map:
                        // Start the MapActivity
                        startActivity(new Intent(ProfileActivity.this, MapActivity.class));
                        finish();
                        return true;
                    case R.id.menu_chat:
                        // Start the ChatActivity
                        startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
                        finish();
                        return true;
                    case R.id.menu_profile:
                        // Start the ProfileActivity
                        startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}