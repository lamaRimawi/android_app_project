package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {

    private RecyclerView offersRecyclerView;
    private OffersAdapter offersAdapter;
    private LinearLayout emptyStateContainer;
    private TextView emptyStateText;

    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    private List<Product> offersList = new ArrayList<>();

    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        return new OffersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadOffers();

        return view;
    }

    private void initViews(View view) {
        offersRecyclerView = view.findViewById(R.id.offers_recycler_view);
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
            offersAdapter = new OffersAdapter(getContext(), offersList, new OffersAdapter.OnOfferClickListener() {
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
            offersRecyclerView.setLayoutManager(layoutManager);
            offersRecyclerView.setAdapter(offersAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.offers_container).startAnimation(fadeIn);
        }
    }

    private void loadOffers() {
        if (dbHelper == null) {
            return;
        }

        offersList.clear();

        try {
            // Get all products with discounts (offers)
            List<Product> discountedProducts = dbHelper.getDiscountedProducts();
            offersList.addAll(discountedProducts);

            if (offersList.isEmpty()) {
                offersRecyclerView.setVisibility(View.GONE);
                emptyStateContainer.setVisibility(View.VISIBLE);
            } else {
                offersRecyclerView.setVisibility(View.VISIBLE);
                emptyStateContainer.setVisibility(View.GONE);
            }

            if (offersAdapter != null) {
                offersAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show empty state on error
            offersRecyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        }
    }

    private void toggleFavorite(Product product) {
        if (prefsManager == null || dbHelper == null) {
            return;
        }

        String userEmail = prefsManager.getUserEmail();
        boolean isFavorite = dbHelper.isFavorite(userEmail, product.getId());

        if (isFavorite) {
            if (dbHelper.removeFromFavorites(userEmail, product.getId())) {
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                // Add animation
                if (getContext() != null) {
                    Animation heartBreak = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                }
            }
        } else {
            if (dbHelper.addToFavorites(userEmail, product.getId())) {
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                // Add heart animation
                if (getContext() != null) {
                    Animation heartBeat = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    // Apply to the heart button if needed
                }
            }
        }

        // Update the adapter to reflect favorite status change
        if (offersAdapter != null) {
            offersAdapter.notifyDataSetChanged();
        }
    }

    private void showOrderDialog(Product product) {
        OrderDialogFragment dialog = OrderDialogFragment.newInstance(product);
        if (getParentFragmentManager() != null) {
            dialog.show(getParentFragmentManager(), "OrderDialog");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh offers when fragment becomes visible
        loadOffers();
        // Also refresh adapter to update favorite status
        if (offersAdapter != null) {
            offersAdapter.notifyDataSetChanged();
        }
    }
}