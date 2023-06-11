package com.example.projectprm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {

    private TextView tvCartInfo;
    private TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        tvCartInfo = findViewById(R.id.tv_cart_info);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        // Retrieve the cart information and total price from the intent extras
        Intent intent = getIntent();
        if (intent != null) {
            List<Cart> cartList = (List<Cart>) intent.getSerializableExtra("cartList");
            double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

            // Display the cart information and total price in the appropriate views
            displayCartInfo(cartList);
            displayTotalPrice(totalPrice);
        }
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
