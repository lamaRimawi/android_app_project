package com.grocery.store.a1213515_1200209_andriod;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;


public class AddAdminFragment extends Fragment {

    // UI Components
    private TextInputEditText adminEmailEditText, adminFirstNameEditText, adminLastNameEditText;
    private TextInputEditText adminPasswordEditText, adminConfirmPasswordEditText, adminPhoneEditText;
    private Spinner adminGenderSpinner, adminCitySpinner;
    private MaterialButton addAdminButton;

    // Database helper
    private DatabaseHelper dbHelper;

    // City to country code mapping
    private Map<String, String> cityCountryCodes = new HashMap<>();

    public AddAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of AddAdminFragment
     */
    public static AddAdminFragment newInstance() {
        return new AddAdminFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());
        initializeCityCountryCodes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        initViews(view);
        setupSpinners();
        setupClickListeners();
        setupAnimations(view);

        return view;
    }

    /**
     * Initialize all UI components
     */
    private void initViews(View view) {
        adminEmailEditText = view.findViewById(R.id.admin_email_edit_text);
        adminFirstNameEditText = view.findViewById(R.id.admin_first_name_edit_text);
        adminLastNameEditText = view.findViewById(R.id.admin_last_name_edit_text);
        adminPasswordEditText = view.findViewById(R.id.admin_password_edit_text);
        adminConfirmPasswordEditText = view.findViewById(R.id.admin_confirm_password_edit_text);
        adminPhoneEditText = view.findViewById(R.id.admin_phone_edit_text);
        adminGenderSpinner = view.findViewById(R.id.admin_gender_spinner);
        adminCitySpinner = view.findViewById(R.id.admin_city_spinner);
        addAdminButton = view.findViewById(R.id.add_admin_button);
    }

    /**
     * Setup spinners with data and listeners
     */
    private void setupSpinners() {
        // Gender Spinner
        String[] genders = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminGenderSpinner.setAdapter(genderAdapter);

        // City Spinner
        String[] cities = {"Select City", "Ramallah", "Nablus", "Hebron", "Gaza", "Bethlehem", "Jenin"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adminCitySpinner.setAdapter(cityAdapter);

        // City selection listener to auto-fill country code
        adminCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                if (cityCountryCodes.containsKey(selectedCity)) {
                    String countryCode = cityCountryCodes.get(selectedCity);
                    adminPhoneEditText.setText(countryCode);
                    adminPhoneEditText.setSelection(adminPhoneEditText.getText().length());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Initialize city to country code mapping
     */
    private void initializeCityCountryCodes() {
        cityCountryCodes.put("Ramallah", "+970");
        cityCountryCodes.put("Nablus", "+970");
        cityCountryCodes.put("Hebron", "+970");
        cityCountryCodes.put("Gaza", "+970");
        cityCountryCodes.put("Bethlehem", "+970");
        cityCountryCodes.put("Jenin", "+970");
    }

    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        addAdminButton.setOnClickListener(v -> attemptAddAdmin());
    }

    /**
     * Setup entrance animations
     */
    private void setupAnimations(View view) {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.findViewById(R.id.add_admin_content).startAnimation(fadeIn);
    }

    /**
     * Attempt to add a new admin user
     */
    private void attemptAddAdmin() {
        String email = adminEmailEditText.getText().toString().trim();
        String firstName = adminFirstNameEditText.getText().toString().trim();
        String lastName = adminLastNameEditText.getText().toString().trim();
        String password = adminPasswordEditText.getText().toString().trim();
        String confirmPassword = adminConfirmPasswordEditText.getText().toString().trim();
        String phone = adminPhoneEditText.getText().toString().trim();
        String gender = adminGenderSpinner.getSelectedItem().toString();
        String city = adminCitySpinner.getSelectedItem().toString();

        // Validate input
        if (!validateInput(email, firstName, lastName, password, confirmPassword, phone, gender, city)) {
            return;
        }

        // Check if email already exists
        if (dbHelper.checkEmailExists(email)) {
            adminEmailEditText.setError("Email already exists");
            adminEmailEditText.requestFocus();
            showErrorAnimation();
            return;
        }

        if (addAdminToDatabase(email, firstName, lastName, password, gender, city, phone)) {
            showSuccessAnimation();
            Toast.makeText(getContext(), "Admin added successfully!", Toast.LENGTH_LONG).show();
            clearForm();
        } else {
            showErrorAnimation();
            Toast.makeText(getContext(), "Failed to add admin. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Add admin to database with admin role
     */
    private boolean addAdminToDatabase(String email, String firstName, String lastName,
                                       String password, String gender, String city, String phone) {
        // Use the addAdmin method that sets the user role as admin
        return dbHelper.addAdmin(email, firstName, lastName, password, gender, city, phone);
    }

    /**
     * Validate all input fields
     */
    private boolean validateInput(String email, String firstName, String lastName,
                                  String password, String confirmPassword, String phone,
                                  String gender, String city) {

        // Email validation
        if (TextUtils.isEmpty(email)) {
            adminEmailEditText.setError("Email is required");
            adminEmailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            adminEmailEditText.setError("Please enter a valid email");
            adminEmailEditText.requestFocus();
            return false;
        }

        // First name validation
        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            adminFirstNameEditText.setError("First name must be at least 3 characters");
            adminFirstNameEditText.requestFocus();
            return false;
        }

        // Last name validation
        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            adminLastNameEditText.setError("Last name must be at least 3 characters");
            adminLastNameEditText.requestFocus();
            return false;
        }

        // Password validation
        if (!isValidPassword(password)) {
            adminPasswordEditText.setError("Password must be at least 5 characters with 1 letter, 1 number, and 1 special character");
            adminPasswordEditText.requestFocus();
            return false;
        }

        // Confirm password validation
        if (!password.equals(confirmPassword)) {
            adminConfirmPasswordEditText.setError("Passwords do not match");
            adminConfirmPasswordEditText.requestFocus();
            return false;
        }

        // Gender validation
        if (gender.equals("Select Gender")) {
            Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        // City validation
        if (city.equals("Select City")) {
            Toast.makeText(getContext(), "Please select city", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Phone validation
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            adminPhoneEditText.setError("Please enter a valid phone number");
            adminPhoneEditText.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Validate password strength
     */
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

    /**
     * Clear all form fields after successful submission
     */
    private void clearForm() {
        adminEmailEditText.setText("");
        adminFirstNameEditText.setText("");
        adminLastNameEditText.setText("");
        adminPasswordEditText.setText("");
        adminConfirmPasswordEditText.setText("");
        adminPhoneEditText.setText("");
        adminGenderSpinner.setSelection(0);
        adminCitySpinner.setSelection(0);
    }

    /**
     * Show success animation
     */
    private void showSuccessAnimation() {
        if (getContext() != null) {
            Animation successAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            addAdminButton.startAnimation(successAnimation);
        }
    }

    /**
     * Show error animation
     */
    private void showErrorAnimation() {
        if (getContext() != null) {
            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            addAdminButton.startAnimation(shake);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}