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

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    private Context context;
    private List<Product> offers;
    private OnOfferClickListener listener;
    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    public interface OnOfferClickListener {
        void onFavoriteClick(Product product);
        void onOrderClick(Product product);
    }

    public OffersAdapter(Context context, List<Product> offers, OnOfferClickListener listener) {
        this.context = context;
        this.offers = offers;
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context);
        this.prefsManager = new SharedPreferencesManager(context);
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Product product = offers.get(position);
        holder.bind(product);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView productImage;
        TextView titleText;
        TextView categoryText;
        TextView priceText;
        TextView originalPriceText;
        TextView discountText;
        TextView stockText;
        TextView ratingText;
        TextView savingsText;
        ImageButton favoriteButton;
        MaterialButton orderButton;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.offer_card);
            productImage = itemView.findViewById(R.id.offer_product_image);
            titleText = itemView.findViewById(R.id.offer_product_title);
            categoryText = itemView.findViewById(R.id.offer_product_category);
            priceText = itemView.findViewById(R.id.offer_product_price);
            originalPriceText = itemView.findViewById(R.id.offer_original_price);
            discountText = itemView.findViewById(R.id.offer_discount_text);
            stockText = itemView.findViewById(R.id.offer_stock_text);
            ratingText = itemView.findViewById(R.id.offer_rating_text);
            savingsText = itemView.findViewById(R.id.offer_savings_text);
            favoriteButton = itemView.findViewById(R.id.offer_favorite_button);
            orderButton = itemView.findViewById(R.id.offer_order_button);
        }

        public void bind(Product product) {
            DecimalFormat df = new DecimalFormat("#.##");

            // Set product title
            titleText.setText(product.getTitle());

            // Set category
            categoryText.setText(product.getCategory());

            // Calculate discounted price and savings
            double originalPrice = product.getPrice();
            double discountPercentage = product.getDiscountPercentage();
            double finalPrice = originalPrice * (1 - discountPercentage / 100);
            double savings = originalPrice - finalPrice;

            // Set prices
            priceText.setText("$" + df.format(finalPrice));
            originalPriceText.setText("$" + df.format(originalPrice));
            originalPriceText.setPaintFlags(originalPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // Set discount percentage
            discountText.setText("-" + (int)discountPercentage + "%");

            // Set savings amount
            savingsText.setText("Save $" + df.format(savings));

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

            // Set favorite button state
            String userEmail = prefsManager.getUserEmail();
            boolean isFavorite = dbHelper.isFavorite(userEmail, product.getId());
            updateFavoriteButton(isFavorite);

            // Set click listeners
            favoriteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(product);
                    // Add heart animation
                    Animation heartBeat = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    favoriteButton.startAnimation(heartBeat);

                    // Update button state
                    boolean newFavoriteState = dbHelper.isFavorite(userEmail, product.getId());
                    updateFavoriteButton(newFavoriteState);
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

            // Card click listener for product details
            cardView.setOnClickListener(v -> {
                Animation ripple = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                cardView.startAnimation(ripple);
            });
        }

        private void updateFavoriteButton(boolean isFavorite) {
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
                favoriteButton.setColorFilter(context.getResources().getColor(R.color.error_red));
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_border);
                favoriteButton.setColorFilter(context.getResources().getColor(R.color.text_secondary));
            }
        }
    }
}