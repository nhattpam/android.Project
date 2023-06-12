package com.example.projectprm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShopActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Get the logged-in username from the intent
        loggedInUsername = getIntent().getStringExtra("username");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_product:
                        openProductActivity();
                        return true;
                    case R.id.menu_map:
                        openMapActivity();
                        return true;
                    case R.id.menu_chat:
                        openChatActivity();
                        return true;
                    case R.id.menu_profile:
                        openProfileActivity();
                        return true;
                }
                return false;
            }
        });

        // Set the default menu item
        bottomNavigationView.setSelectedItemId(R.id.menu_product);
    }

    private void openProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }

    private void openChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }
}
