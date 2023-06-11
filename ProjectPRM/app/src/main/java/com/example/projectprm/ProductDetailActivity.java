package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class ProductDetailActivity extends AppCompatActivity {
    private List<Cart> cartList;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductDescription = findViewById(R.id.tv_product_description);
        tvProductPrice = findViewById(R.id.tv_product_price);

        // Get the selected product from the intent
        selectedProduct = (Product) getIntent().getSerializableExtra("product");

        if (selectedProduct != null) {
            ivProductImage.setImageResource(selectedProduct.getImageResourceId());
            tvProductName.setText(selectedProduct.getName());
            tvProductDescription.setText(selectedProduct.getDescription());
            tvProductPrice.setText(selectedProduct.getPrice());
        }

        // Check if the cartList is already initialized
        if (savedInstanceState != null) {
            cartList = (List<Cart>) savedInstanceState.getSerializable("cartList");
        } else {
            // Initialize the cartList for the first time
            cartList = new ArrayList<>();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("cartList", (Serializable) cartList);
    }

}
