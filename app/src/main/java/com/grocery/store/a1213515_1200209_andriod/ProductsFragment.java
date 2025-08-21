package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.slider.RangeSlider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private RangeSlider priceRangeSlider;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;
    private ApiService apiService;
    private SharedPreferencesManager prefsManager;

    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupSearchAndFilters();
        setupAnimations(view);
        loadProducts();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.products_recycler_view);
        searchEditText = view.findViewById(R.id.search_edit_text);
        categorySpinner = view.findViewById(R.id.category_spinner);
        priceRangeSlider = view.findViewById(R.id.price_range_slider);
        progressBar = view.findViewById(R.id.progress_bar);

        prefsManager = new SharedPreferencesManager(getContext());
    }

    private void setupDatabase() {
        dbHelper = new DatabaseHelper(getContext());
    }

    private void setupRecyclerView() {
        adapter = new ProductsAdapter(getContext(), filteredProducts, new ProductsAdapter.OnProductClickListener() {
            @Override
            public void onFavoriteClick(Product product) {
                toggleFavorite(product);
            }

            @Override
            public void onOrderClick(Product product) {
                showOrderDialog(product);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchAndFilters() {
        // Setup search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup category spinner with grocery categories
        categories.clear();
        categories.add("All Categories");
        categories.add("Fruits");
        categories.add("Vegetables");
        categories.add("Dairy");
        categories.add("Meat");
        categories.add("Beverages");
        categories.add("Bakery");
        categories.add("Snacks");
        categories.add("Frozen");
        categories.add("Canned");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Setup price range slider
        priceRangeSlider.setValueFrom(0f);
        priceRangeSlider.setValueTo(200f);
        priceRangeSlider.setValues(0f, 200f);
        priceRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                filterProducts();
            }
        });
    }

    private void setupAnimations(View view) {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.findViewById(R.id.products_container).startAnimation(fadeIn);
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        // First try to load from local database
        allProducts = dbHelper.getAllProducts();

        if (allProducts.isEmpty()) {
            // If no local products, fetch from API
            fetchProductsFromAPI();
        } else {
            // Load categories and display products
            loadCategories();
            filteredProducts.clear();
            filteredProducts.addAll(allProducts);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void fetchProductsFromAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        Call<ProductResponse> call = apiService.getGroceryProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getProducts() != null) {

                    allProducts = response.body().getProducts();

                    // Save products to local database
                    for (Product product : allProducts) {
                        dbHelper.addProduct(product);
                    }

                    loadCategories();
                    filteredProducts.clear();
                    filteredProducts.addAll(allProducts);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Products loaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    showError("Failed to load products");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void loadCategories() {

        if (categorySpinner.getAdapter() != null) {
            ((ArrayAdapter) categorySpinner.getAdapter()).notifyDataSetChanged();
        }
    }

    private void filterProducts() {
        String query = searchEditText.getText().toString().toLowerCase().trim();
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        List<Float> priceRange = priceRangeSlider.getValues();
        float minPrice = priceRange.get(0);
        float maxPrice = priceRange.get(1);

        filteredProducts.clear();

        for (Product product : allProducts) {
            boolean matchesSearch = query.isEmpty() ||
                    product.getTitle().toLowerCase().contains(query) ||
                    product.getCategory().toLowerCase().contains(query);

            boolean matchesCategory = selectedCategory.equals("All Categories") ||
                    product.getCategory().equals(selectedCategory);

            boolean matchesPrice = product.getPrice() >= minPrice && product.getPrice() <= maxPrice;

            if (matchesSearch && matchesCategory && matchesPrice) {
                filteredProducts.add(product);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void toggleFavorite(Product product) {
        String userEmail = prefsManager.getUserEmail();
        boolean isFavorite = dbHelper.isFavorite(userEmail, product.getId());

        if (isFavorite) {
            if (dbHelper.removeFromFavorites(userEmail, product.getId())) {
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                // Add animation
                Animation heartBreak = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                // Apply to the heart button if needed
            }
        } else {
            if (dbHelper.addToFavorites(userEmail, product.getId())) {
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                // Add heart animation
                Animation heartBeat = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                // Apply to the heart button if needed
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void showOrderDialog(Product product) {
        OrderDialogFragment dialog = OrderDialogFragment.newInstance(product);
        dialog.show(getParentFragmentManager(), "OrderDialog");
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh adapter to update favorite status
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}