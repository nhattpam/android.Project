package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import android.widget.Toast;

import java.util.List;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static List<Cart> cartList;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;

    private Button btnCheckout;

    private String username;
    private int userId;

    public static List<Cart> getCartList() {
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        return cartList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycler_view_cart);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Get the cartList from the CartActivity
        List<Cart> cartList = CartActivity.getCartList();

        // Check if the cartList is null or empty
        if (cartList == null || cartList.isEmpty()) {
            // Show an empty cart message or perform any other action
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            setupRecyclerView();
            updateCartUI();
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the cart information and total price to the BillActivity
                Intent intent = new Intent(CartActivity.this, BillActivity.class);
                intent.putExtra("cartList", (Serializable) cartList);
                intent.putExtra("totalPrice", calculateTotalPrice());
                // Save the bill to the database
                intent.putExtra("username", username); // Pass the username
                saveBillToDatabase();
                startActivity(intent);
            }
        });


    }
    private int getUserId() {
        // Initialize your database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Retrieve the userId based on the username
        userId = databaseHelper.getUserIdByUsername(username);
        return userId;
    }
    private void saveBillToDatabase() {
        // Initialize your database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Save the bill information to the database
        for (Cart cart : cartList) {
            int productId = cart.getProduct().getId();
            int quantity = cart.getQuantity();

            // Save the bill details using the productId, quantity, and username
            databaseHelper.addBill(productId, quantity, getUserId(), calculateTotalPrice());
        }
    }





    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(CartActivity.getCartList());
        recyclerView.setAdapter(cartAdapter);
    }

    private void updateCartUI() {
        double totalPrice = calculateTotalPrice();
        tvTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        List<Cart> cartList = CartActivity.getCartList();
        for (Cart cart : cartList) {
            String priceString = cart.getProduct().getPrice();
            // Remove the "$" symbol from the price string
            String priceWithoutSymbol = priceString.replace("$", "");
            double price = Double.parseDouble(priceWithoutSymbol);
            totalPrice += price * cart.getQuantity();
        }
        return totalPrice;
    }
}

