package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartList = (List<Cart>) getIntent().getSerializableExtra("cartList");

        recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartList);
        recyclerView.setAdapter(cartAdapter);

        tvTotalPrice = findViewById(R.id.tv_total_price);
        updateCartUI();
    }

    private void updateCartUI() {
        if (cartList == null || cartList.isEmpty()) {
            // Show an empty cart message or perform any other action
        } else {
            cartAdapter.notifyDataSetChanged();
        }

        double totalPrice = 0;
        tvTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));
    }

//    private double calculateTotalPrice() {
//        double totalPrice = 0;
//        for (Cart cart : cartList) {
//            double price = Double.parseDouble(cart.getProduct().getPrice());
//            totalPrice += price * cart.getQuantity();
//        }
//        return totalPrice;
//    }
}
