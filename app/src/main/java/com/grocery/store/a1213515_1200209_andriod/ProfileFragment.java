package com.grocery.store.a1213515_1200209_andriod;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText currentPasswordEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton changePhotoButton;
    private MaterialButton saveChangesButton;

    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    private Uri selectedImageUri;
    private String currentUserEmail;

    // Activity result launchers
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityResultLaunchers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        setupDatabase();
        setupAnimations(view);
        loadUserProfile();
        setupClickListeners();

        return view;
    }

    private void setupActivityResultLaunchers() {
        // Image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            selectedImageUri = imageUri;
                            displaySelectedImage(imageUri);
                        }
                    }
                }
        );

        // Permission launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openImagePicker();
                    } else {
                        Toast.makeText(getContext(), "Permission denied to access gallery", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initViews(View view) {
        profileImageView = view.findViewById(R.id.profile_image);
        firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        currentPasswordEditText = view.findViewById(R.id.current_password_edit_text);
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        changePhotoButton = view.findViewById(R.id.change_photo_button);
        saveChangesButton = view.findViewById(R.id.save_changes_button);

        if (getContext() != null) {
            prefsManager = new SharedPreferencesManager(getContext());
            currentUserEmail = prefsManager.getUserEmail();
        }
    }

    private void setupDatabase() {
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
        }
    }

    private void setupAnimations(View view) {
        if (getContext() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            view.findViewById(R.id.profile_container).startAnimation(fadeIn);
        }
    }

    private void loadUserProfile() {
        if (dbHelper == null || currentUserEmail == null) {
            return;
        }

        try {
            UserProfile userProfile = dbHelper.getUserProfile(currentUserEmail);
            if (userProfile != null) {
                firstNameEditText.setText(userProfile.getFirstName());
                lastNameEditText.setText(userProfile.getLastName());
                phoneEditText.setText(userProfile.getPhone());

                // Load profile image if exists
                if (userProfile.getProfileImagePath() != null && !userProfile.getProfileImagePath().isEmpty()) {
                    loadProfileImage(userProfile.getProfileImagePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage(String imagePath) {
        if (getContext() != null) {
            Glide.with(getContext())
                    .load(imagePath)
                    .placeholder(R.drawable.ic_person_large)
                    .error(R.drawable.ic_person_large)
                    .circleCrop()
                    .into(profileImageView);
        }
    }

    private void setupClickListeners() {
        changePhotoButton.setOnClickListener(v -> checkPermissionAndOpenGallery());
        saveChangesButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void checkPermissionAndOpenGallery() {
        // For development simplicity, we'll use a basic approach
        openImagePicker();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void displaySelectedImage(Uri imageUri) {
        if (getContext() != null) {
            Glide.with(getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.ic_person_large)
                    .error(R.drawable.ic_person_large)
                    .circleCrop()
                    .into(profileImageView);

            // Add animation
            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            profileImageView.startAnimation(scaleUp);
        }
    }

    private void saveProfileChanges() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate input
        if (!validateProfileInput(firstName, lastName, phone, currentPassword, newPassword, confirmPassword)) {
            return;
        }

        try {
            // Check current password if user wants to change password
            boolean passwordChangeRequested = !TextUtils.isEmpty(newPassword);
            if (passwordChangeRequested) {
                if (!dbHelper.checkUser(currentUserEmail, currentPassword)) {
                    currentPasswordEditText.setError("Current password is incorrect");
                    currentPasswordEditText.requestFocus();
                    return;
                }
            }

            // Prepare profile image path
            String profileImagePath = null;
            if (selectedImageUri != null) {
                profileImagePath = selectedImageUri.toString();
            } else {
                // Keep existing image path
                UserProfile existingProfile = dbHelper.getUserProfile(currentUserEmail);
                if (existingProfile != null) {
                    profileImagePath = existingProfile.getProfileImagePath();
                }
            }

            // Update profile
            boolean success;
            if (passwordChangeRequested) {
                success = dbHelper.updateUserProfile(currentUserEmail, firstName, lastName, phone, newPassword, profileImagePath);
            } else {
                success = dbHelper.updateUserProfileWithoutPassword(currentUserEmail, firstName, lastName, phone, profileImagePath);
            }

            if (success) {
                // Add success animation
                if (getContext() != null) {
                    Animation successAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    saveChangesButton.startAnimation(successAnim);
                }

                Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_LONG).show();

                // Clear password fields
                currentPasswordEditText.setText("");
                newPasswordEditText.setText("");
                confirmPasswordEditText.setText("");

            } else {
                Toast.makeText(getContext(), "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateProfileInput(String firstName, String lastName, String phone,
                                         String currentPassword, String newPassword, String confirmPassword) {

        // Validate first name
        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            firstNameEditText.setError("First name must be at least 3 characters");
            firstNameEditText.requestFocus();
            return false;
        }

        // Validate last name
        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            lastNameEditText.setError("Last name must be at least 3 characters");
            lastNameEditText.requestFocus();
            return false;
        }

        // Validate phone
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            phoneEditText.setError("Please enter a valid phone number");
            phoneEditText.requestFocus();
            return false;
        }

        // Validate password change if requested
        boolean passwordChangeRequested = !TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(confirmPassword);

        if (passwordChangeRequested) {
            // Current password is required
            if (TextUtils.isEmpty(currentPassword)) {
                currentPasswordEditText.setError("Current password is required");
                currentPasswordEditText.requestFocus();
                return false;
            }

            // Validate new password
            if (!isValidPassword(newPassword)) {
                newPasswordEditText.setError("Password must be at least 5 characters with 1 letter, 1 number, and 1 special character");
                newPasswordEditText.requestFocus();
                return false;
            }

            // Confirm password
            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                confirmPasswordEditText.requestFocus();
                return false;
            }
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 5) return false;

        boolean hasLetter = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isWhitespace(c)) hasSpecial = true;
        }

        return hasLetter && hasNumber && hasSpecial;
    }
}