package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    // Initialize your SQLite database helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Initialize your database helper
        databaseHelper = new DatabaseHelper(this);

        // Populate the productList with sample data or fetch from the database
        populateProductList();

        // Set click listener for the product items
        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Retrieve the clicked product from the productList
                Product clickedProduct = productList.get(position);
                // Launch the ProductDetailActivity and pass the clicked product details
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", clickedProduct);
                startActivity(intent);
            }
        });
    }

    private void populateProductList() {
        // Clear the existing list
        productList.clear();

        // Add sample products or fetch from the database
        productList.add(new Product(1, "Product 1", "Description 1", "10.99", R.drawable.gigabyte));
        productList.add(new Product(2, "Product 2", "Description 2", "19.99", R.drawable.hp));
        productList.add(new Product(3, "Product 3", "Description 3", "15.99", R.drawable.lenovo));


        // Notify the adapter that the data set has changed
        productAdapter.notifyDataSetChanged();
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
}
