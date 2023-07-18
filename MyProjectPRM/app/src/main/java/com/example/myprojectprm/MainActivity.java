package com.example.myprojectprm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameContainer;

    private ProductFragment productFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;
    private ProfileFragment profileFragment;

    private MyProfileFragment myProfileFragment;

    private String loggedInUsername; // Variable to store the username of the logged-in user

    private static final String CHANNEL_ID = "cart_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private static List<Cart> cartList;

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
        myProfileFragment = new MyProfileFragment();

        // Set the default fragment
        setFragment(productFragment);

        //notification when cart has products
        // Retrieve the saved cart data from SharedPreferences
        // Retrieve the saved cart data from SharedPreferences
        SharedPreferences sharedPreferences2 = getSharedPreferences(AppConstants.CART_PREFS_NAME, MODE_PRIVATE);
        String cartListJson = sharedPreferences2.getString("cartList", "");


        if (!cartListJson.isEmpty()) {
            // Convert the JSON string back to the list of Cart objects using Gson
            Gson gson = new Gson();
            Type cartListType = new TypeToken<List<Cart>>() {}.getType();
            cartList = gson.fromJson(cartListJson, cartListType);
            if(cartList.size() > 0)
            {
                showNotificationIfNeeded();
            }
        }

        // Check if the openCartFragment flag is set
        if (getIntent().getBooleanExtra("openCartFragment", false)) {
            openCartFragment();
        }

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
//                    case R.id.menu_chat:
//                        setFragment(chatFragment);
//                        return true;
                    case R.id.menu_profile:
                        setFragment(profileFragment);
                        return true;
                    case R.id.menu_my_profile:
                        setFragment(myProfileFragment);
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

    //menu bar: cart, logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);

        // Add the logout icon to the menu
        MenuItem logoutItem = menu.add(Menu.NONE, R.id.action_logout, Menu.NONE, "Logout");
        logoutItem.setIcon(R.drawable.ic_logout);
        return true;
    }

    //OPEN cart Screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            // Open the cart activity
            openCartFragment();
            return true;
        } else if(itemId == R.id.action_logout){
            //Logout: khang
            logoutUser();
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

    //Logout: khang

    private void logoutUser() {
        // Clear the saved username and password from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("isLoggedIn"); // Remove the isLoggedIn flag
        editor.apply();

        // Navigate back to the login screen
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // Finish the MainActivity to prevent returning to it when pressing back
    }

    //notify if cart has products
    private void showNotificationIfNeeded() {
        // Check if the cart has products and show a notification if it does
        if (cartList.size() > 0) {
            // Build and show the notification
            buildAndShowNotification();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, build and show the notification
                buildAndShowNotification();
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Permission denied to show notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buildAndShowNotification() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("openCartFragment", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Cart Notification")
                .setContentText("Your cart has products")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Cart Notifications";
            String channelDescription = "Notification channel for cart updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationChannel.setDescription(channelDescription);

            // Register the channel with the system
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}