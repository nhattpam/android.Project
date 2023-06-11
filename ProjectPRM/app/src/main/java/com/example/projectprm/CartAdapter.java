package com.example.projectprm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartList;

    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.bind(cart);
    }

    //remove cart item
    private OnRemoveItemClickListener removeItemClickListener;

    public void setRemoveItemClickListener(OnRemoveItemClickListener listener) {
        this.removeItemClickListener = listener;
    }

    public interface OnRemoveItemClickListener {
        void onRemoveItemClick(int position);
    }
    //end remove cart item

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductQuantity;

        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove_from_cart);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            removeItemClickListener.onRemoveItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Cart cart) {
            Product product = cart.getProduct();

            // Set the product details in the views
            ivProductImage.setImageResource(product.getImageResourceId());
            tvProductName.setText(product.getName());

            double price = Double.parseDouble(product.getPrice());
            tvProductPrice.setText(String.format("Price: $%.2f", price));

            tvProductQuantity.setText(String.format("Quantity: %d", cart.getQuantity()));
        }

    }


}
