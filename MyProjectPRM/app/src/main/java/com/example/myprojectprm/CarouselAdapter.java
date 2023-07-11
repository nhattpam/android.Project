package com.example.myprojectprm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private List<CarouselItem> carouselItems;

    public CarouselAdapter(List<CarouselItem> carouselItems) {
        this.carouselItems = carouselItems;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        CarouselItem item = carouselItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return carouselItems.size();
    }

    public class CarouselViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImageView);
        }

        public void bind(CarouselItem item) {
            imageView.setImageResource(item.getImageResId());
        }
    }
}
