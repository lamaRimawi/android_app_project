package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.activity.OnBackPressedCallback;
import com.google.android.material.navigation.NavigationView;
import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferencesManager prefsManager;

    private void setupBackPressHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is admin and redirect if necessary
        prefsManager = new SharedPreferencesManager(this);
        if (prefsManager.isAdmin()) {
            Intent intent = new Intent(MainActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);


        try {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.updateProductCategories();
        } catch (Exception e) {

            e.printStackTrace();
        }

        initViews();
        setupToolbar();
        setupNavigationDrawer();
        setupUserInfo();
        setupBackPressHandler();


        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
            setTitle(getString(R.string.menu_home));
        }
    }
    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        prefsManager = new SharedPreferencesManager(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupUserInfo() {
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.nav_user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.nav_user_email);
        ImageView userImageView = headerView.findViewById(R.id.nav_user_image);

        String userEmail = prefsManager.getUserEmail();
        boolean isAdmin = prefsManager.isAdmin();

        if (userEmailTextView != null) {
            userEmailTextView.setText(userEmail);
        }

        if (userNameTextView != null) {
            if (isAdmin) {
                userNameTextView.setText("Administrator");
            } else {

                DatabaseHelper dbHelper = null;
                try {
                    dbHelper = new DatabaseHelper(this);
                    UserProfile userProfile = dbHelper.getUserProfile(userEmail);
                    if (userProfile != null && userProfile.getFirstName() != null) {
                        userNameTextView.setText(userProfile.getFullName());

                        // Load profile image if available
                        if (userImageView != null && userProfile.getProfileImagePath() != null && !userProfile.getProfileImagePath().isEmpty()) {
                            loadProfileImage(userImageView, userProfile.getProfileImagePath());
                        }
                    } else {
                        userNameTextView.setText("Customer");
                    }
                } catch (Exception e) {

                    userNameTextView.setText("Customer");
                } finally {
                    if (dbHelper != null) {
                        dbHelper.close();
                    }
                }
            }
        }
    }

    private void loadProfileImage(ImageView imageView, String imagePath) {
        try {
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                // Handle URI
                Uri imageUri = Uri.parse(imagePath);
                imageView.setImageURI(imageUri);
            } else {
                // Handle file path
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.ic_person);
                    }
                } else {
                    imageView.setImageResource(R.drawable.ic_person);
                }
            }
        } catch (Exception e) {
            // If image loading fails, show default icon
            imageView.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String title = "";

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            title = getString(R.string.menu_home);
        } else if (id == R.id.nav_products) {
            fragment = new ProductsFragment();
            title = getString(R.string.menu_products);
        } else if (id == R.id.nav_my_orders) {
            fragment = new MyOrdersFragment();
            title = getString(R.string.menu_orders);
        } else if (id == R.id.nav_favorites) {
            fragment = new FavoritesFragment();
            title = getString(R.string.menu_favorites);
        } else if (id == R.id.nav_offers) {
            fragment = new OffersFragment();
            title = getString(R.string.menu_offers);
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            title = getString(R.string.menu_profile);
        } else if (id == R.id.nav_contact) {
            fragment = new ContactFragment();
            title = getString(R.string.menu_contact);
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
            return true;
        }

        if (fragment != null) {
            loadFragment(fragment);
            setTitle(title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void logout() {
        prefsManager.clearLoginSession();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}