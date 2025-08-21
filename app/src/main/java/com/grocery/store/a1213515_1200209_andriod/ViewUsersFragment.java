package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewUsersFragment extends Fragment {

    private RecyclerView usersRecyclerView;
    private UsersViewAdapter usersAdapter;
    private DatabaseHelper dbHelper;
    private List<UserProfile> usersList = new ArrayList<>();

    public ViewUsersFragment() {
        // Required empty public constructor
    }

    public static ViewUsersFragment newInstance() {
        return new ViewUsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        initViews(view);
        setupDatabase();
        setupRecyclerView();
        setupAnimations(view);
        loadUsers();

        return view;
    }

    private void initViews(View view) {
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupRecyclerView() {
        if (getContext() != null) {
            usersAdapter = new UsersViewAdapter(getContext(), usersList);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            usersRecyclerView.setLayoutManager(layoutManager);
            usersRecyclerView.setAdapter(usersAdapter);
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.view_users_content).startAnimation(fadeIn);
        }
    }

    private void loadUsers() {
        if (dbHelper == null) return;

        usersList.clear();
        try {
            List<UserProfile> allUsers = dbHelper.getAllCustomers();
            usersList.addAll(allUsers);
            usersAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading users", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUsers();
    }
}