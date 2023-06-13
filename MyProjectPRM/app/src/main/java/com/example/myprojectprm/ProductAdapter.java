package com.example.myprojectprm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);

        // Set click listener for "Add to Cart" icon
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Add to Cart" click event
                addToCart(product, holder.itemView.getContext());
            }
        });
    }

    private void addToCart(Product product, Context context) {
        List<Cart> cartList = CartFragment.getCartList();

        boolean isProductAlreadyInCart = false;

        // Iterate through the cartList to check if the product is already in the cart
        for (Cart cart : cartList) {
            if (cart.getProduct().equals(product)) {
                // Product is already in the cart, update the quantity
                cart.setQuantity(cart.getQuantity() + 1);
                isProductAlreadyInCart = true;
                break;
            }
        }

        if (!isProductAlreadyInCart) {
            // Product is not in the cart, add a new Cart object
            Cart cart = new Cart(product, 1);
            cartList.add(cart);
        }

        // Show a toast message indicating the product was added to cart
        Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);

            // Set click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Product product) {
            ivProductImage.setImageResource(product.getImageResourceId());
            tvProductName.setText(product.getName());
            tvProductPrice.setText(product.getPrice());
        }
    }
}
