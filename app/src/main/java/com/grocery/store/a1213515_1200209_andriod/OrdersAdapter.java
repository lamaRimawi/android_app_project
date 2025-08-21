package com.grocery.store.a1213515_1200209_andriod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private SimpleDateFormat dateFormat;
    private DecimalFormat priceFormat;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        this.priceFormat = new DecimalFormat("#.##");
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView orderCard;
        ImageView productImage;
        TextView productNameText;
        TextView quantityText;
        TextView dateTimeText;
        TextView statusText;
        TextView totalPriceText;
        TextView deliveryMethodText;
        TextView orderIdText;
        View statusIndicator;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCard = itemView.findViewById(R.id.order_card);
            productImage = itemView.findViewById(R.id.order_product_image);
            productNameText = itemView.findViewById(R.id.order_product_name);
            quantityText = itemView.findViewById(R.id.order_quantity);
            dateTimeText = itemView.findViewById(R.id.order_date_time);
            statusText = itemView.findViewById(R.id.order_status);
            totalPriceText = itemView.findViewById(R.id.order_total_price);
            deliveryMethodText = itemView.findViewById(R.id.order_delivery_method);
            orderIdText = itemView.findViewById(R.id.order_id);
            statusIndicator = itemView.findViewById(R.id.status_indicator);
        }

        public void bind(Order order) {
            // Set product name
            productNameText.setText(order.getProductName() != null ? order.getProductName() : "Unknown Product");

            // Set quantity
            quantityText.setText("Qty: " + order.getQuantity());

            // Set date and time
            Date orderDate = new Date(order.getOrderDate());
            dateTimeText.setText(dateFormat.format(orderDate));

            // Set status with color coding
            String status = order.getStatus();
            statusText.setText(status);
            setStatusColor(status);

            // Set total price
            totalPriceText.setText("$" + priceFormat.format(order.getTotalPrice()));

            // Set delivery method
            deliveryMethodText.setText(order.getDeliveryMethod());

            // Set order ID
            orderIdText.setText("Order #" + order.getOrderId());

            // Load product image
            if (order.getProductThumbnail() != null && !order.getProductThumbnail().isEmpty()) {
                Glide.with(context)
                        .load(order.getProductThumbnail())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        }

        private void setStatusColor(String status) {
            int statusColor;
            int indicatorColor;

            switch (status.toLowerCase()) {
                case "pending":
                    statusColor = ContextCompat.getColor(context, R.color.accent_orange);
                    indicatorColor = ContextCompat.getColor(context, R.color.accent_orange);
                    break;
                case "approved":
                    statusColor = ContextCompat.getColor(context, R.color.primary_green);
                    indicatorColor = ContextCompat.getColor(context, R.color.primary_green);
                    break;
                case "delivered":
                    statusColor = ContextCompat.getColor(context, R.color.primary_green);
                    indicatorColor = ContextCompat.getColor(context, R.color.primary_green);
                    break;
                case "cancelled":
                    statusColor = ContextCompat.getColor(context, R.color.error_red);
                    indicatorColor = ContextCompat.getColor(context, R.color.error_red);
                    break;
                default:
                    statusColor = ContextCompat.getColor(context, R.color.text_secondary);
                    indicatorColor = ContextCompat.getColor(context, R.color.text_secondary);
                    break;
            }

            statusText.setTextColor(statusColor);
            statusIndicator.setBackgroundColor(indicatorColor);
        }
    }
}