package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    // Initialize your SQLite database helper
    private DatabaseHelper databaseHelper;
    private static final String TABLE_PRODUCTS = "products";

    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";

    private String loggedInUsername; // Variable to store the username of the logged-in user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Get the logged-in username from the intent
        loggedInUsername = getIntent().getStringExtra("username");

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

    private void openProductActivity() {
        // Already in the ProductActivity, do nothing
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
        finish();
    }

    private void openChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
        finish();
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
        finish();
    }

    private void populateProductList() {
        // Clear the existing list
        productList.clear();

        // Add sample products or fetch from the database
        productList.add(new Product(1, "Product 1", "Description 1", "10.99", R.drawable.gigabyte));
        productList.add(new Product(2, "Product 2", "Description 2", "19.99", R.drawable.hp));
        productList.add(new Product(3, "Product 3", "Description 3", "15.99", R.drawable.lenovo));

        // Save the products to the database
        for (Product product : productList) {
            saveProductToDatabase(product);
        }

        // Notify the adapter that the data set has changed
        productAdapter.notifyDataSetChanged();
    }

    private void saveProductToDatabase(Product product) {
        // Get a writable instance of the database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to hold the product values
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
        // Add more columns as needed

        // Insert the values into the products table
        long productId = db.insert(TABLE_PRODUCTS, null, values);

        // Set the generated product ID in the product object
        product.setId((int) productId);

        // Close the database connection
        db.close();
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
        intent.putExtra("username", loggedInUsername); // Pass the username to the CartActivity
        startActivity(intent);
    }
}
