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

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private List<Cart> cartList;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;
    private Button btnAddToCart;

    //cart


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Instantiate the Cart

        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductDescription = findViewById(R.id.tv_product_description);
        tvProductPrice = findViewById(R.id.tv_product_price);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        // Get the selected product from the intent
        Product selectedProduct = (Product) getIntent().getSerializableExtra("product");

        if (selectedProduct != null) {
            ivProductImage.setImageResource(selectedProduct.getImageResourceId());
            tvProductName.setText(selectedProduct.getName());
            tvProductDescription.setText(selectedProduct.getDescription());
            tvProductPrice.setText(selectedProduct.getPrice());
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the product to the cart
                addToCart(selectedProduct);
            }
        });
    }

    //hien thi icon cart
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_cart) {
            // Open the cart activity or perform any action you desire
            openCartActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    //cart
    private void addToCart(Product product) {

        Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show();
    }
}
