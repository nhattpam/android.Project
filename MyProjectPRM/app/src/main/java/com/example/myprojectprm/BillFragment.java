package com.example.myprojectprm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


public class BillFragment extends Fragment {
    private TextView tvCartInfo;
    private TextView tvTotalPrice;
    private TextView tvUsername;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);


        tvCartInfo = view.findViewById(R.id.tv_cart_info);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        tvUsername = view.findViewById(R.id.tv_username);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String username = bundle.getString("username");
            tvUsername.setText("Customer with username: " + username);
            List<Cart> cartList = (List<Cart>) bundle.getSerializable("cartList");
            double totalPrice = bundle.getDouble("totalPrice", 0.0);

            // Display the cart information and total price in the appropriate views
            displayCartInfo(cartList);
            displayTotalPrice(totalPrice);
        }
        return view;
    }

    private void displayCartInfo(List<Cart> cartList) {
        StringBuilder builder = new StringBuilder();
        for (Cart cart : cartList) {
            // Customize the cart item display format according to your preference
            builder.append(cart.getProduct().getName())
                    .append(" - Quantity: ")
                    .append(cart.getQuantity())
                    .append(System.lineSeparator());
        }
        tvCartInfo.setText(builder.toString());
    }

    private void displayTotalPrice(double totalPrice) {
        String priceText = String.format(Locale.getDefault(), "Total Price: $%.2f", totalPrice);
        tvTotalPrice.setText(priceText);
    }
}