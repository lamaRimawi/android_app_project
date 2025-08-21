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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ManageProductsFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private AdminProductsAdapter productsAdapter;
    private FloatingActionButton addProductFab;
    private DatabaseHelper dbHelper;
    private List<Product> productsList = new ArrayList<>();

    public ManageProductsFragment() {
        // Required empty public constructor
    }

    public static ManageProductsFragment newInstance() {
        return new ManageProductsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_products, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        setupClickListeners();
        loadProducts();

        return view;
    }

    private void initViews(View view) {
        productsRecyclerView = view.findViewById(R.id.manage_products_recycler_view);
        addProductFab = view.findViewById(R.id.add_product_fab);
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupRecyclerView() {
        if (getContext() != null) {
            productsAdapter = new AdminProductsAdapter(getContext(), productsList, new AdminProductsAdapter.OnProductClickListener() {
                @Override
                public void onEditClick(Product product) {
                    showEditProductDialog(product);
                }

                @Override
                public void onDeleteClick(Product product) {
                    showDeleteConfirmationDialog(product);
                }
            });

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            productsRecyclerView.setLayoutManager(layoutManager);
            productsRecyclerView.setAdapter(productsAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.manage_products_content).startAnimation(fadeIn);
        }
    }

    private void setupClickListeners() {
        addProductFab.setOnClickListener(v -> {
            showAddProductDialog();
            Animation bounce = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            addProductFab.startAnimation(bounce);
        });
    }

    private void loadProducts() {
        if (dbHelper == null) return;

        productsList.clear();
        try {
            List<Product> allProducts = dbHelper.getAllProducts();
            productsList.addAll(allProducts);
            productsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading products", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddProductDialog() {
        AddProductDialogFragment dialog = AddProductDialogFragment.newInstance();
        dialog.setOnProductAddedListener(() -> loadProducts());
        dialog.show(getParentFragmentManager(), "AddProductDialog");
    }

    private void showEditProductDialog(Product product) {
        EditProductDialogFragment dialog = EditProductDialogFragment.newInstance(product);
        dialog.setOnProductUpdatedListener(() -> loadProducts());
        dialog.show(getParentFragmentManager(), "EditProductDialog");
    }

    private void showDeleteConfirmationDialog(Product product) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete: " + product.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteProduct(Product product) {
        if (dbHelper.deleteProduct(product.getId())) {
            Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
            loadProducts();
        } else {
            Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts();
    }
}