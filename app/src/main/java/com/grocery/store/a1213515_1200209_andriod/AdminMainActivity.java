package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_admin_main);

        initViews();
        setupToolbar();
        setupNavigationDrawer();
        setupAdminInfo();
        setupBackPressHandler();

        // Load admin dashboard by default
        if (savedInstanceState == null) {
            loadFragment(new AdminDashboardFragment());
            navigationView.setCheckedItem(R.id.nav_admin_home);
            setTitle("Admin Dashboard");
        }
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.admin_drawer_layout);
        navigationView = findViewById(R.id.admin_nav_view);
        toolbar = findViewById(R.id.admin_toolbar);
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

    private void setupAdminInfo() {
        // Update navigation header for admin
        if (navigationView.getHeaderView(0) != null) {
            TextView userNameTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
            TextView userEmailTextView = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);

            if (userNameTextView != null) {
                userNameTextView.setText("Administrator");
            }
            if (userEmailTextView != null) {
                userEmailTextView.setText(prefsManager.getUserEmail());
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        String title = "";

        if (id == R.id.nav_admin_home) {
            fragment = new AdminDashboardFragment();
            title = "Admin Dashboard";
        } else if (id == R.id.nav_manage_products) {
            fragment = new ManageProductsFragment();
            title = "Manage Products";
        } else if (id == R.id.nav_manage_orders) {
            fragment = new ManageOrdersFragment();
            title = "Manage Orders";
        } else if (id == R.id.nav_view_users) {
            fragment = new ViewUsersFragment();
            title = "View Users";
        } else if (id == R.id.nav_delete_customers) {
            fragment = new DeleteCustomersFragment();
            title = "Delete Customers";
        } else if (id == R.id.nav_add_admin) {
            fragment = new AddAdminFragment();
            title = "Add New Admin";
        } else if (id == R.id.nav_admin_logout) {
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
        transaction.replace(R.id.admin_fragment_container, fragment);
        transaction.commit();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Admin Logout")
                .setMessage("Are you sure you want to logout from admin panel?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        prefsManager.clearLoginSession();
        Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Admin logged out successfully", Toast.LENGTH_SHORT).show();
    }
}