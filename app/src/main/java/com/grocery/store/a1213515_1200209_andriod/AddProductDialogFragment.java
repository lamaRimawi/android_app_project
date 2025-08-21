package com.grocery.store.a1213515_1200209_andriod;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddProductDialogFragment extends DialogFragment {

    private OnProductAddedListener listener;
    private DatabaseHelper dbHelper;
    private EditText etProductId, etTitle, etDescription, etPrice, etDiscount, etRating, etStock, etBrand, etThumbnail;
    private Spinner spinnerCategory;

    public interface OnProductAddedListener {
        void onProductAdded();
    }

    public static AddProductDialogFragment newInstance() {
        return new AddProductDialogFragment();
    }

    public void setOnProductAddedListener(OnProductAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dbHelper = new DatabaseHelper(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_product, null);

        initViews(view);
        setupCategorySpinner();

        return new AlertDialog.Builder(requireContext())
                .setTitle("Add New Product")
                .setView(view)
                .setPositiveButton("Add Product", null) // Set null initially
                .setNegativeButton("Cancel", null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if (validateAndAddProduct()) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void initViews(View view) {
        etProductId = view.findViewById(R.id.et_product_id);
        etTitle = view.findViewById(R.id.et_title);
        etDescription = view.findViewById(R.id.et_description);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        etPrice = view.findViewById(R.id.et_price);
        etDiscount = view.findViewById(R.id.et_discount);
        etRating = view.findViewById(R.id.et_rating);
        etStock = view.findViewById(R.id.et_stock);
        etBrand = view.findViewById(R.id.et_brand);
        etThumbnail = view.findViewById(R.id.et_thumbnail);
    }

    private void setupCategorySpinner() {
        String[] categories = {
                "Fruits", "Vegetables", "Dairy", "Meat", "Beverages",
                "Frozen", "Canned", "Health", "Household", "Pet Food"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }
    private boolean validateAndAddProduct() {
        // Validate required fields
        if (TextUtils.isEmpty(etProductId.getText().toString().trim())) {
            etProductId.setError("Product ID is required");
            return false;
        }

        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            etTitle.setError("Product title is required");
            return false;
        }

        if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            etPrice.setError("Price is required");
            return false;
        }

        try {
            // Parse and validate numeric fields
            int productId = Integer.parseInt(etProductId.getText().toString().trim());
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            double discount = TextUtils.isEmpty(etDiscount.getText().toString().trim()) ?
                    0.0 : Double.parseDouble(etDiscount.getText().toString().trim());
            double rating = TextUtils.isEmpty(etRating.getText().toString().trim()) ?
                    0.0 : Double.parseDouble(etRating.getText().toString().trim());
            int stock = TextUtils.isEmpty(etStock.getText().toString().trim()) ?
                    0 : Integer.parseInt(etStock.getText().toString().trim());

            // Validate ranges
            if (price <= 0) {
                etPrice.setError("Price must be greater than 0");
                return false;
            }

            if (discount < 0 || discount > 100) {
                etDiscount.setError("Discount must be between 0 and 100");
                return false;
            }

            if (rating < 0 || rating > 5) {
                etRating.setError("Rating must be between 0 and 5");
                return false;
            }

            if (stock < 0) {
                etStock.setError("Stock cannot be negative");
                return false;
            }

            // Create product object
            Product product = new Product();
            product.setId(productId);
            product.setTitle(etTitle.getText().toString().trim());
            product.setDescription(etDescription.getText().toString().trim());
            product.setCategory(spinnerCategory.getSelectedItem().toString());
            product.setPrice(price);
            product.setDiscountPercentage(discount);
            product.setRating(rating);
            product.setStock(stock);
            product.setBrand(etBrand.getText().toString().trim());
            product.setThumbnail(etThumbnail.getText().toString().trim());

            // Add to database
            if (dbHelper.addProduct(product)) {
                Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onProductAdded();
                }
                return true;
            } else {
                Toast.makeText(getContext(), "Failed to add product. Product ID might already exist.", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}