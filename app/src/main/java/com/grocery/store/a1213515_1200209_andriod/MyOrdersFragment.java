package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private LinearLayout emptyStateContainer;
    private TextView emptyStateText;

    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    private List<Order> ordersList = new ArrayList<>();

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance() {
        return new MyOrdersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadOrders();

        return view;
    }

    private void initViews(View view) {
        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        emptyStateContainer = view.findViewById(R.id.empty_state_container);
        emptyStateText = view.findViewById(R.id.empty_state_text);

        if (getContext() != null) {
            prefsManager = new SharedPreferencesManager(getContext());
        }
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupRecyclerView() {
        if (getContext() != null) {
            ordersAdapter = new OrdersAdapter(getContext(), ordersList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            ordersRecyclerView.setLayoutManager(layoutManager);
            ordersRecyclerView.setAdapter(ordersAdapter);
        }
    }

    private void setupAnimations(View view) {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.findViewById(R.id.orders_container).startAnimation(fadeIn);
    }

    private void loadOrders() {
        String userEmail = prefsManager.getUserEmail();
        ordersList.clear();

        List<Order> userOrders = dbHelper.getUserOrders(userEmail);
        ordersList.addAll(userOrders);

        if (ordersList.isEmpty()) {
            ordersRecyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            ordersRecyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }

        ordersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh orders when fragment becomes visible
        loadOrders();
    }
}