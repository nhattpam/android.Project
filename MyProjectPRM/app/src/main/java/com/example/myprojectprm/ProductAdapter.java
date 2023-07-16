package com.example.myprojectprm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>   implements Filterable {

    private List<Product> productList;
    //tao 1 list i chang listProduct de ktra du lieu
    private List<Product> mListProductOld;
    private Context mContext;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.mContext = context;
        this.productList = productList;
        this.mListProductOld = productList;

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
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //truyen object product qua detail
                onClickGoToDetailProduct(product);
            }
        });


    }
    private void onClickGoToDetailProduct(Product product) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        // Pass the clicked product details to the fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        productDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, productDetailFragment)
                .addToBackStack(null) // Add the fragment to the back stack to enable back navigation
                .commit();
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
            Date orderDate = new Date();
            Cart cart = new Cart(product, 1, orderDate);
            cartList.add(cart);
        }
// Save the updated cart data to SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.CART_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String cartListJson = gson.toJson(cartList);

        editor.putString("cartList", cartListJson);
        editor.apply();

        // Show a toast message indicating the product was added to cart
        Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        //click
        private RelativeLayout layoutItem;
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            //click
            layoutItem = itemView.findViewById(R.id.layout_item);
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
            tvProductPrice.setText("$ " + product.getPrice());
        }


    }
    //ham search nam trong Product Fragment
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    productList = mListProductOld;
                }else{
                    List<Product> list = new ArrayList<>();
                    for (Product product: mListProductOld){
                        if(product.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(product);
                        }
                    }
                    productList = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
