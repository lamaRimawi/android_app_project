package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ManageOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private AdminOrdersAdapter ordersAdapter;
    private DatabaseHelper dbHelper;
    private List<Order> ordersList = new ArrayList<>();

    public ManageOrdersFragment() {
        // Required empty public constructor
    }

    public static ManageOrdersFragment newInstance() {
        return new ManageOrdersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_orders, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadOrders();

        return view;
    }

    private void initViews(View view) {
        ordersRecyclerView = view.findViewById(R.id.manage_orders_recycler_view);
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupRecyclerView() {
        if (getContext() != null) {
            ordersAdapter = new AdminOrdersAdapter(getContext(), ordersList, new AdminOrdersAdapter.OnOrderClickListener() {
                @Override
                public void onChangeStatusClick(Order order) {
                    showStatusChangeDialog(order);
                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            ordersRecyclerView.setLayoutManager(layoutManager);
            ordersRecyclerView.setAdapter(ordersAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.manage_orders_content).startAnimation(fadeIn);
        }
    }

    private void loadOrders() {
        if (dbHelper == null) return;

        ordersList.clear();
        try {
            List<Order> allOrders = dbHelper.getAllOrders();
            ordersList.addAll(allOrders);
            ordersAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading orders", Toast.LENGTH_SHORT).show();
        }
    }

    private void showStatusChangeDialog(Order order) {
        String[] statusOptions = {"Pending", "Approved", "Delivered", "Cancelled"};

        new AlertDialog.Builder(getContext())
                .setTitle("Change Order Status")
                .setItems(statusOptions, (dialog, which) -> {
                    String newStatus = statusOptions[which];
                    updateOrderStatus(order, newStatus);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateOrderStatus(Order order, String newStatus) {
        if (dbHelper.updateOrderStatus(order.getOrderId(), newStatus)) {
            Toast.makeText(getContext(), "Order status updated to: " + newStatus, Toast.LENGTH_SHORT).show();
            loadOrders(); // Refresh the list
        } else {
            Toast.makeText(getContext(), "Failed to update order status", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}