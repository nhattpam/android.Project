package com.example.myprojectprm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private List<Bill> billList;

    public BillAdapter(List<Bill> billList) {
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);

        // Bind the bill data to the views in the ViewHolder
        holder.bind(bill);
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {

        private TextView billIdTextView;
        private TextView productIdTextView;
        private TextView quantityTextView;
        private TextView totalPriceTextView;
        private TextView orderDateTextView;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            billIdTextView = itemView.findViewById(R.id.bill_id_text_view);
            productIdTextView = itemView.findViewById(R.id.product_id_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            totalPriceTextView = itemView.findViewById(R.id.total_price_text_view);
            orderDateTextView = itemView.findViewById(R.id.order_date_text_view);
        }

        public void bind(Bill bill) {
            billIdTextView.setText("Bill ID: " + String.valueOf(bill.getBillId()));
            productIdTextView.setText("Product ID: " + String.valueOf(bill.getProductId()));
            quantityTextView.setText("Quantity: " + String.valueOf(bill.getQuantity()));
            totalPriceTextView.setText("Total Price: " + String.valueOf(bill.getTotalPrice()) + " $");
            orderDateTextView.setText("Order Date: " + String.valueOf(bill.getOrderDate()));
        }
    }
}
