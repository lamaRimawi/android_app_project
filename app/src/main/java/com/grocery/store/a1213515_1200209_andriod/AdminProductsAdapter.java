package com.grocery.store.a1213515_1200209_andriod;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.AdminProductViewHolder> {

    private Context context;
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public AdminProductsAdapter(Context context, List<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class AdminProductViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView productImage;
        TextView titleText;
        TextView categoryText;
        TextView priceText;
        TextView originalPriceText;
        TextView discountText;
        TextView stockText;
        TextView ratingText;
        MaterialButton editButton;
        MaterialButton deleteButton;

        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.admin_product_card);
            productImage = itemView.findViewById(R.id.admin_product_image);
            titleText = itemView.findViewById(R.id.admin_product_title);
            categoryText = itemView.findViewById(R.id.admin_product_category);
            priceText = itemView.findViewById(R.id.admin_product_price);
            originalPriceText = itemView.findViewById(R.id.admin_original_price);
            discountText = itemView.findViewById(R.id.admin_discount_text);
            stockText = itemView.findViewById(R.id.admin_stock_text);
            ratingText = itemView.findViewById(R.id.admin_rating_text);
            editButton = itemView.findViewById(R.id.edit_product_button);
            deleteButton = itemView.findViewById(R.id.delete_product_button);
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
                stockText.setText("Stock: " + product.getStock());
                stockText.setTextColor(context.getResources().getColor(R.color.primary_green));
            } else {
                stockText.setText("Out of Stock");
                stockText.setTextColor(context.getResources().getColor(R.color.error_red));
            }

            // Set rating
            ratingText.setText("⭐ " + df.format(product.getRating()));

            // Load product image using Glide
            if (product.getThumbnail() != null && !product.getThumbnail().isEmpty()) {
                Glide.with(context)
                        .load(product.getThumbnail())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_image_placeholder);
            }

            // Set click listeners
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(product);
                    // Add button animation
                    Animation bounce = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    editButton.startAnimation(bounce);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(product);
                    // Add button animation
                    Animation bounce = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    deleteButton.startAnimation(bounce);
                }
            });

            // Card click listener for product details
            cardView.setOnClickListener(v -> {
                Animation ripple = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                cardView.startAnimation(ripple);
            });
        }
    }
}