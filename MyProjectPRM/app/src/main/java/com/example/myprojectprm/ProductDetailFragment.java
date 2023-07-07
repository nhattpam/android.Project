package com.example.myprojectprm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProductDetailFragment extends Fragment {

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductDescription;
    private TextView tvProductPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Find views by their IDs
        ivProductImage = view.findViewById(R.id.iv_product_image);
        tvProductName = view.findViewById(R.id.tv_product_name);
        tvProductDescription = view.findViewById(R.id.tv_product_description);
        tvProductPrice = view.findViewById(R.id.tv_product_price);

        // Retrieve the product data from the arguments
        Bundle bundle = getArguments();
        if (bundle  != null && bundle .containsKey("product")) {
            Product product = (Product) bundle.getSerializable("product");
            if (product != null) {
                // Set the product details to the views
                ivProductImage.setImageResource(product.getImageResourceId());
                tvProductName.setText(product.getName());
                tvProductDescription.setText(product.getDescription());
                tvProductPrice.setText("$ " + product.getPrice());
            }
        }

        return view;
    }
}

