package com.grocery.store.a1213515_1200209_andriod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder> {

    private Context context;
    private List<UserProfile> customers;
    private OnCustomerClickListener listener;

    public interface OnCustomerClickListener {
        void onDeleteClick(UserProfile customer);
    }

    public CustomersAdapter(Context context, List<UserProfile> customers, OnCustomerClickListener listener) {
        this.context = context;
        this.customers = customers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        UserProfile customer = customers.get(position);
        holder.bind(customer);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView customerCard;
        TextView nameText, emailText, phoneText, cityText, genderText;
        MaterialButton deleteButton;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerCard = itemView.findViewById(R.id.customer_card);
            nameText = itemView.findViewById(R.id.customer_name);
            emailText = itemView.findViewById(R.id.customer_email);
            phoneText = itemView.findViewById(R.id.customer_phone);
            cityText = itemView.findViewById(R.id.customer_city);
            genderText = itemView.findViewById(R.id.customer_gender);
            deleteButton = itemView.findViewById(R.id.delete_customer_button);
        }

        public void bind(UserProfile customer) {
            nameText.setText(customer.getFullName());
            emailText.setText(customer.getEmail());
            phoneText.setText(customer.getPhone());
            cityText.setText(customer.getCity());
            genderText.setText(customer.getGender());

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(customer);
                    Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                    deleteButton.startAnimation(shake);
                }
            });
        }
    }
}