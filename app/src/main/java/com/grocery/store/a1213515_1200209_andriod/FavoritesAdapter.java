package com.grocery.store.a1213515_1200209_andriod;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.text.DecimalFormat;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Product> favorites;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onRemoveFromFavorites(Product product);
        void onOrderClick(Product product);
    }

    public FavoritesAdapter(Context context, List<Product> favorites, OnFavoriteClickListener listener) {
        this.context = context;
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = favorites.get(position);
        holder.bind(product);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView productImage;
        TextView titleText;
        TextView categoryText;
        TextView priceText;
        TextView originalPriceText;
        TextView discountText;
        TextView stockText;
        TextView ratingText;
        ImageButton removeButton;
        MaterialButton orderButton;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.favorite_card);
            productImage = itemView.findViewById(R.id.favorite_product_image);
            titleText = itemView.findViewById(R.id.favorite_product_title);
            categoryText = itemView.findViewById(R.id.favorite_product_category);
            priceText = itemView.findViewById(R.id.favorite_product_price);
            originalPriceText = itemView.findViewById(R.id.favorite_original_price);
            discountText = itemView.findViewById(R.id.favorite_discount_text);
            stockText = itemView.findViewById(R.id.favorite_stock_text);
            ratingText = itemView.findViewById(R.id.favorite_rating_text);
            removeButton = itemView.findViewById(R.id.remove_favorite_button);
            orderButton = itemView.findViewById(R.id.favorite_order_button);
        }

        public void bind(Product product) {
            DecimalFormat df = new DecimalFormat("#.##");

            // Set product title
            titleText.setText(product.getTitle());

            // Set category
            categoryText.setText(product.getCategory());

            // Calculate discounted price if there's a discount
            double finalPrice = product.getPrice();
            if (product.getDiscountPercentage() > 0) {
                finalPrice = product.getPrice() * (1 - product.getDiscountPercentage() / 100);
                priceText.setText("$" + df.format(finalPrice));
                originalPriceText.setText("$" + df.format(product.getPrice()));
                originalPriceText.setPaintFlags(originalPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                originalPriceText.setVisibility(View.VISIBLE);
                discountText.setText("-" + (int)product.getDiscountPercentage() + "%");
                discountText.setVisibility(View.VISIBLE);
            } else {
                priceText.setText("$" + df.format(product.getPrice()));
                originalPriceText.setVisibility(View.GONE);
                discountText.setVisibility(View.GONE);
            }

            // Set stock status
            if (product.getStock() > 0) {
                stockText.setText("In Stock (" + product.getStock() + ")");
                stockText.setTextColor(context.getResources().getColor(R.color.primary_green));
                orderButton.setEnabled(true);
                orderButton.setAlpha(1.0f);
            } else {
                stockText.setText("Out of Stock");
                stockText.setTextColor(context.getResources().getColor(R.color.error_red));
                orderButton.setEnabled(false);
                orderButton.setAlpha(0.5f);
            }

            // Set rating
            ratingText.setText("⭐ " + df.format(product.getRating()));

            // Load product image using Glide
            Glide.with(context)
                    .load(product.getThumbnail())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(productImage);

            // Set remove button - always filled heart since it's in favorites
            removeButton.setImageResource(R.drawable.ic_favorite_filled);
            removeButton.setColorFilter(context.getResources().getColor(R.color.error_red));

            // Set click listeners
            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveFromFavorites(product);
                    // Add heart break animation
                    Animation heartBreak = AnimationUtils.loadAnimation(context, R.anim.scale_down);
                    removeButton.startAnimation(heartBreak);
                }
            });

            orderButton.setOnClickListener(v -> {
                if (listener != null && product.getStock() > 0) {
                    listener.onOrderClick(product);
                    // Add button animation
                    Animation bounce = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    orderButton.startAnimation(bounce);
                }
            });

            // Card click listener for product details (optional enhancement)
            cardView.setOnClickListener(v -> {
                Animation ripple = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                cardView.startAnimation(ripple);
            });
        }
    }
}