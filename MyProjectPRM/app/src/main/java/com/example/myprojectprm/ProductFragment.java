package com.example.myprojectprm;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper databaseHelper;
    private static final String TABLE_PRODUCTS = "products";

    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";

    private static final String CHANNEL_ID = "cart_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private static List<Cart> cartList;

    private String loggedInUsername; // Variable to store the username of the logged-in user

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //notification when cart has products
        // Retrieve the saved cart data from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppConstants.CART_PREFS_NAME, MODE_PRIVATE);
        String cartListJson = sharedPreferences.getString("cartList", "");

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



        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

// Retrieve the passed username from the arguments
        loggedInUsername = getArguments().getString("username");
//        Log.d("product", loggedInUsername);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        databaseHelper = new DatabaseHelper(requireContext());

        populateProductList();

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Product clickedProduct = productList.get(position);

                // Create a new instance of the ProductDetailFragment
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();

                // Pass the clicked product details to the fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", clickedProduct);
                productDetailFragment.setArguments(bundle);

                // Replace the current fragment with the ProductDetailFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, productDetailFragment)
                        .addToBackStack(null) // Add the fragment to the back stack to enable back navigation
                        .commit();
            }
        });

    }

    private void populateProductList() {
        productList.clear();

        productList.add(new Product(1, "Product 1", "Description 1", "10.99", R.drawable.gigabyte));
        productList.add(new Product(2, "Product 2", "Description 2", "19.99", R.drawable.hp));
        productList.add(new Product(3, "Product 3", "Description 3", "15.99", R.drawable.lenovo));

        for (Product product : productList) {
            saveProductToDatabase(product);
        }

        productAdapter.notifyDataSetChanged();
    }

    private void saveProductToDatabase(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());

        long productId = db.insert(TABLE_PRODUCTS, null, values);

        product.setId((int) productId);

        db.close();
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
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, build and show the notification
                buildAndShowNotification();
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                Toast.makeText(requireContext(), "Permission denied to show notifications", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buildAndShowNotification() {
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Cart Notification")
                .setContentText("Your cart has products")
                .setAutoCancel(true);

        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Cart Notifications";
            String channelDescription = "Notification channel for cart updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationChannel.setDescription(channelDescription);

            // Register the channel with the system
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
