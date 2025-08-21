package com.grocery.store.a1213515_1200209_andriod;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.text.DecimalFormat;

public class OrderDialogFragment extends DialogFragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_TITLE = "product_title";
    private static final String ARG_PRODUCT_PRICE = "product_price";
    private static final String ARG_PRODUCT_DISCOUNT = "product_discount";
    private static final String ARG_PRODUCT_STOCK = "product_stock";
    private static final String ARG_PRODUCT_THUMBNAIL = "product_thumbnail";

    private int productId;
    private String productTitleStr;
    private double productPriceValue;
    private double productDiscountValue;
    private int productStockValue;
    private String productThumbnailUrl;
    private ImageView productImage;
    private TextView productTitle;
    private TextView productPrice;
    private EditText quantityEditText;
    private RadioGroup deliveryMethodGroup;
    private RadioButton pickupRadio;
    private RadioButton homeDeliveryRadio;
    private TextView totalPriceText;
    private Button confirmButton;
    private Button cancelButton;

    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    public static OrderDialogFragment newInstance(Product product) {
        OrderDialogFragment fragment = new OrderDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, product.getId());
        args.putString(ARG_PRODUCT_TITLE, product.getTitle());
        args.putDouble(ARG_PRODUCT_PRICE, product.getPrice());
        args.putDouble(ARG_PRODUCT_DISCOUNT, product.getDiscountPercentage());
        args.putInt(ARG_PRODUCT_STOCK, product.getStock());
        args.putString(ARG_PRODUCT_THUMBNAIL, product.getThumbnail());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
            productTitleStr = getArguments().getString(ARG_PRODUCT_TITLE);
            productPriceValue = getArguments().getDouble(ARG_PRODUCT_PRICE);
            productDiscountValue = getArguments().getDouble(ARG_PRODUCT_DISCOUNT);
            productStockValue = getArguments().getInt(ARG_PRODUCT_STOCK);
            productThumbnailUrl = getArguments().getString(ARG_PRODUCT_THUMBNAIL);
        }
        dbHelper = new DatabaseHelper(getContext());
        prefsManager = new SharedPreferencesManager(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_order, container, false);

        initViews(view);
        setupData();
        setupListeners();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = onCreateView(getLayoutInflater(), null, savedInstanceState);

        return new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .create();
    }

    private void initViews(View view) {
        productImage = view.findViewById(R.id.dialog_product_image);
        productTitle = view.findViewById(R.id.dialog_product_title);
        productPrice = view.findViewById(R.id.dialog_product_price);
        quantityEditText = view.findViewById(R.id.quantity_edit_text);
        deliveryMethodGroup = view.findViewById(R.id.delivery_method_group);
        pickupRadio = view.findViewById(R.id.pickup_radio);
        homeDeliveryRadio = view.findViewById(R.id.home_delivery_radio);
        totalPriceText = view.findViewById(R.id.total_price_text);
        confirmButton = view.findViewById(R.id.confirm_button);
        cancelButton = view.findViewById(R.id.cancel_button);
    }

    private void setupData() {
        if (productTitleStr != null) {
            DecimalFormat df = new DecimalFormat("#.##");

            // Set product details
            productTitle.setText(productTitleStr);

            // Calculate final price (with discount if any)
            double finalPrice = productPriceValue;
            if (productDiscountValue > 0) {
                finalPrice = productPriceValue * (1 - productDiscountValue / 100);
            }
            productPrice.setText("$" + df.format(finalPrice));

            // Load product image
            Glide.with(this)
                    .load(productThumbnailUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_placeholder)
                    .into(productImage);

            // Set default values
            quantityEditText.setText("1");
            pickupRadio.setChecked(true);

            // Calculate initial total
            updateTotalPrice();
        }
    }

    private void setupListeners() {
        // Quantity change listener
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTotalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Delivery method change listener
        deliveryMethodGroup.setOnCheckedChangeListener((group, checkedId) -> updateTotalPrice());

        // Confirm button
        confirmButton.setOnClickListener(v -> confirmOrder());

        // Cancel button
        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void updateTotalPrice() {
        try {
            String quantityStr = quantityEditText.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                totalPriceText.setText("$0.00");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                totalPriceText.setText("$0.00");
                return;
            }

            // Check stock availability
            if (quantity > productStockValue) {
                quantityEditText.setError("Only " + productStockValue + " items available");
                confirmButton.setEnabled(false);
                return;
            } else {
                quantityEditText.setError(null);
                confirmButton.setEnabled(true);
            }

            // Calculate base price
            double unitPrice = productPriceValue;
            if (productDiscountValue > 0) {
                unitPrice = productPriceValue * (1 - productDiscountValue / 100);
            }

            double subtotal = unitPrice * quantity;

            // Add delivery fee if home delivery is selected
            double deliveryFee = 0;
            if (homeDeliveryRadio.isChecked()) {
                deliveryFee = 5.0; // $5 delivery fee
            }

            double total = subtotal + deliveryFee;

            DecimalFormat df = new DecimalFormat("#.##");
            totalPriceText.setText("$" + df.format(total));

        } catch (NumberFormatException e) {
            totalPriceText.setText("$0.00");
        }
    }

    private void confirmOrder() {
        try {
            String quantityStr = quantityEditText.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(getContext(), "Please enter valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity > productStockValue) {
                Toast.makeText(getContext(), "Not enough stock available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get delivery method
            String deliveryMethod = pickupRadio.isChecked() ? "Pickup" : "Home Delivery";

            // Calculate total price
            double unitPrice = productPriceValue;
            if (productDiscountValue > 0) {
                unitPrice = productPriceValue * (1 - productDiscountValue / 100);
            }

            double subtotal = unitPrice * quantity;
            double deliveryFee = homeDeliveryRadio.isChecked() ? 5.0 : 0;
            double totalPrice = subtotal + deliveryFee;

            // Save order to database
            String userEmail = prefsManager.getUserEmail();
            boolean orderSaved = dbHelper.addOrder(userEmail, productId, quantity, deliveryMethod, totalPrice);

            if (orderSaved) {
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_LONG).show();



                dismiss();
            } else {
                Toast.makeText(getContext(), "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid quantity", Toast.LENGTH_SHORT).show();
        }
    }


}