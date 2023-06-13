package com.example.myprojectprm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    private String loggedInUsername; // Variable to store the username of the logged-in user

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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


}
