package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView totalUsersText, totalProductsText, totalOrdersText, pendingOrdersText;
    private MaterialCardView usersCard, productsCard, ordersCard, pendingCard;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    public static AdminDashboardFragment newInstance() {
        return new AdminDashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        initViews(view);
        setupDatabase();
        setupAnimations(view);
        loadDashboardData();

        return view;
    }

    private void initViews(View view) {
        totalUsersText = view.findViewById(R.id.total_users_text);
        totalProductsText = view.findViewById(R.id.total_products_text);
        totalOrdersText = view.findViewById(R.id.total_orders_text);
        pendingOrdersText = view.findViewById(R.id.pending_orders_text);

        usersCard = view.findViewById(R.id.users_card);
        productsCard = view.findViewById(R.id.products_card);
        ordersCard = view.findViewById(R.id.orders_card);
        pendingCard = view.findViewById(R.id.pending_card);
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.admin_dashboard_content).startAnimation(fadeIn);

            // Animate cards with delay
            Animation slideInLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            Animation slideInRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);

            usersCard.startAnimation(slideInLeft);
            ordersCard.startAnimation(slideInLeft);

            productsCard.startAnimation(slideInRight);
            pendingCard.startAnimation(slideInRight);
        }
    }

    private void loadDashboardData() {
        if (dbHelper == null) return;

        try {
            // Get statistics
            int totalUsers = dbHelper.getTotalUsersCount();
            int totalProducts = dbHelper.getTotalProductsCount();
            int totalOrders = dbHelper.getTotalOrdersCount();
            int pendingOrders = dbHelper.getPendingOrdersCount();

            // Update UI
            totalUsersText.setText(String.valueOf(totalUsers));
            totalProductsText.setText(String.valueOf(totalProducts));
            totalOrdersText.setText(String.valueOf(totalOrders));
            pendingOrdersText.setText(String.valueOf(pendingOrders));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment becomes visible
        loadDashboardData();
    }
}