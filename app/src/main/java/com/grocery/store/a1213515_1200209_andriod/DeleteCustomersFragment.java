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

public class DeleteCustomersFragment extends Fragment {

    private RecyclerView customersRecyclerView;
    private CustomersAdapter customersAdapter;
    private DatabaseHelper dbHelper;
    private List<UserProfile> customersList = new ArrayList<>();

    public DeleteCustomersFragment() {
        // Required empty public constructor
    }

    public static DeleteCustomersFragment newInstance() {
        return new DeleteCustomersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_customers, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadCustomers();

        return view;
    }

    private void initViews(View view) {
        customersRecyclerView = view.findViewById(R.id.customers_recycler_view);
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupRecyclerView() {
        if (getContext() != null) {
            customersAdapter = new CustomersAdapter(getContext(), customersList, new CustomersAdapter.OnCustomerClickListener() {
                @Override
                public void onDeleteClick(UserProfile customer) {
                    showDeleteConfirmationDialog(customer);
                }
            });

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            customersRecyclerView.setLayoutManager(layoutManager);
            customersRecyclerView.setAdapter(customersAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.delete_customers_content).startAnimation(fadeIn);
        }
    }

    private void loadCustomers() {
        if (dbHelper == null) return;

        customersList.clear();
        try {
            List<UserProfile> allCustomers = dbHelper.getAllCustomers();
            customersList.addAll(allCustomers);
            customersAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading customers", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(UserProfile customer) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Customer")
                .setMessage("Are you sure you want to delete customer: " + customer.getFullName() + "?\n\nThis action cannot be undone and will also delete all their orders and favorites.")
                .setPositiveButton("Delete", (dialog, which) -> deleteCustomer(customer))
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteCustomer(UserProfile customer) {
        if (dbHelper.deleteCustomer(customer.getEmail())) {
            Toast.makeText(getContext(), "Customer deleted successfully", Toast.LENGTH_SHORT).show();
            loadCustomers(); // Refresh the list
        } else {
            Toast.makeText(getContext(), "Failed to delete customer", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCustomers();
    }
}