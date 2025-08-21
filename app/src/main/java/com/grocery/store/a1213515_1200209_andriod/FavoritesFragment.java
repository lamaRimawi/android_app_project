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

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;
    private LinearLayout emptyStateContainer;
    private TextView emptyStateText;

    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    private List<Product> favoritesList = new ArrayList<>();

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadFavorites();

        return view;
    }

    private void initViews(View view) {
        favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
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
            favoritesAdapter = new FavoritesAdapter(getContext(), favoritesList, new FavoritesAdapter.OnFavoriteClickListener() {
                @Override
                public void onRemoveFromFavorites(Product product) {
                    removeFromFavorites(product);
                }

                @Override
                public void onOrderClick(Product product) {
                    showOrderDialog(product);
                }
            });

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            favoritesRecyclerView.setLayoutManager(layoutManager);
            favoritesRecyclerView.setAdapter(favoritesAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.favorites_container).startAnimation(fadeIn);
        }
    }

    private void loadFavorites() {
        if (prefsManager == null || dbHelper == null) {
            return;
        }

        String userEmail = prefsManager.getUserEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            return;
        }

        favoritesList.clear();

        try {
            List<Product> userFavorites = dbHelper.getUserFavorites(userEmail);
            favoritesList.addAll(userFavorites);

            if (favoritesList.isEmpty()) {
                favoritesRecyclerView.setVisibility(View.GONE);
                emptyStateContainer.setVisibility(View.VISIBLE);
            } else {
                favoritesRecyclerView.setVisibility(View.VISIBLE);
                emptyStateContainer.setVisibility(View.GONE);
            }

            if (favoritesAdapter != null) {
                favoritesAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show empty state on error
            favoritesRecyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        }
    }

    private void removeFromFavorites(Product product) {
        if (prefsManager == null || dbHelper == null) {
            return;
        }

        String userEmail = prefsManager.getUserEmail();
        if (dbHelper.removeFromFavorites(userEmail, product.getId())) {
            Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

            // Add animation for removal
            if (getContext() != null) {
                Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                // Apply animation and reload favorites
                loadFavorites();
            }
        } else {
            Toast.makeText(getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
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
        // Refresh favorites when fragment becomes visible
        loadFavorites();
    }
}