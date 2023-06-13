package com.example.myprojectprm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Retrieve the passed username from the arguments
        username = getArguments().getString("username");

        recyclerView = view.findViewById(R.id.recycler_view_cart);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        // Get the cartList from the CartActivity
        List<Cart> cartList = CartFragment.getCartList();

        // Check if the cartList is null or empty
        if (cartList == null || cartList.isEmpty()) {
            // Show an empty cart message or perform any other action
            Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
            updateCartUI();
        } else {
            setupRecyclerView();
            updateCartUI();
            //remove item from cart
            cartAdapter.setRemoveItemClickListener(new CartAdapter.OnRemoveItemClickListener() {
                @Override
                public void onRemoveItemClick(int position) {
                    // Handle the remove item action
                    removeFromCart(position);
                }
            });
        }

        //checkout
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new instance of the BillFragment
                BillFragment billFragment = new BillFragment();
                // Pass the cart information and total price to the BillActivity
                Bundle bundle = new Bundle();
                bundle.putSerializable("cartList", (Serializable) cartList);
                bundle.putDouble("totalPrice", calculateTotalPrice());
                bundle.putString("username", username);// Pass the username
                billFragment.setArguments(bundle);
                // Save the bill to the database
                saveBillToDatabase();
                // Replace the current fragment with the ProductDetailFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, billFragment)
                        .addToBackStack(null) // Add the fragment to the back stack to enable back navigation
                        .commit();
            }
        });

        return view;
    }

    private int getUserId() {
        // Initialize your database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        // Retrieve the userId based on the username
        userId = databaseHelper.getUserIdByUsername(username);
        return userId;
    }
    private void saveBillToDatabase() {
        // Initialize your database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        // Save the bill information to the database
        for (Cart cart : cartList) {
            int productId = cart.getProduct().getId();
            int quantity = cart.getQuantity();

            // Save the bill details using the productId, quantity, and username
            databaseHelper.addBill(productId, quantity, getUserId(), calculateTotalPrice());
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(CartFragment.getCartList());
        recyclerView.setAdapter(cartAdapter);
    }

    private void updateCartUI() {
        double totalPrice = calculateTotalPrice();
        tvTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));

        // Disable the checkout button if the cart is empty
        if (cartList.isEmpty() || cartList == null) {
            btnCheckout.setEnabled(false);
        } else {
            btnCheckout.setEnabled(true);
        }
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        List<Cart> cartList = CartFragment.getCartList();
        for (Cart cart : cartList) {
            String priceString = cart.getProduct().getPrice();
            // Remove the "$" symbol from the price string
            String priceWithoutSymbol = priceString.replace("$", "");
            double price = Double.parseDouble(priceWithoutSymbol);
            totalPrice += price * cart.getQuantity();
        }
        return totalPrice;
    }

    //method remove item cart
    private void removeFromCart(int position) {
        // Remove the item from the cart list
        cartList.remove(position);

        // Notify the adapter about the item removal
        cartAdapter.notifyItemRemoved(position);

        // Update the total price and UI
        updateCartUI();
    }
}