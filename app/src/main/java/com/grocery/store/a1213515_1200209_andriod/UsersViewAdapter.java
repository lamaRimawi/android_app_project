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
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.UserViewHolder> {

    private Context context;
    private List<UserProfile> users;

    public UsersViewAdapter(Context context, List<UserProfile> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_view, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile user = users.get(position);
        holder.bind(user);

        // Add item animation
        Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        holder.itemView.startAnimation(slideIn);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView userCard;
        TextView nameText, emailText, phoneText, cityText, genderText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userCard = itemView.findViewById(R.id.user_view_card);
            nameText = itemView.findViewById(R.id.user_view_name);
            emailText = itemView.findViewById(R.id.user_view_email);
            phoneText = itemView.findViewById(R.id.user_view_phone);
            cityText = itemView.findViewById(R.id.user_view_city);
            genderText = itemView.findViewById(R.id.user_view_gender);
        }

        public void bind(UserProfile user) {
            nameText.setText(user.getFullName());
            emailText.setText(user.getEmail());
            phoneText.setText(user.getPhone());
            cityText.setText(user.getCity());
            genderText.setText(user.getGender());
        }
    }
}