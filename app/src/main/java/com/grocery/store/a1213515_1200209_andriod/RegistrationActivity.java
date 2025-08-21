package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditText, firstNameEditText, lastNameEditText;
    private EditText passwordEditText, confirmPasswordEditText, phoneEditText;
    private Spinner genderSpinner, citySpinner;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    private Map<String, String> cityCountryCodes = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
        setupDatabase();
        setupSpinners();
        setupAnimations();
        setupClickListeners();
        initializeCityCountryCodes();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        genderSpinner = findViewById(R.id.gender_spinner);
        citySpinner = findViewById(R.id.city_spinner);
        registerButton = findViewById(R.id.register_button);
    }

    private void setupDatabase() {
        dbHelper = new DatabaseHelper(this);
    }

    private void setupSpinners() {
        String[] genders = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        String[] cities = {"Select City", "Ramallah", "Nablus", "Hebron", "Gaza", "Bethlehem", "Jenin"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                if (cityCountryCodes.containsKey(selectedCity)) {
                    String countryCode = cityCountryCodes.get(selectedCity);
                    phoneEditText.setText(countryCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initializeCityCountryCodes() {
        cityCountryCodes.put("Ramallah", "+970");
        cityCountryCodes.put("Nablus", "+970");
        cityCountryCodes.put("Hebron", "+970");
        cityCountryCodes.put("Gaza", "+970");
        cityCountryCodes.put("Bethlehem", "+970");
        cityCountryCodes.put("Jenin", "+970");
    }

    private void setupAnimations() {
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        findViewById(R.id.registration_container).startAnimation(fadeIn);
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        String email = emailEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();

        if (!validateInput(email, firstName, lastName, password, confirmPassword, phone, gender, city)) {
            return;
        }

        if (dbHelper.checkEmailExists(email)) {
            emailEditText.setError("Email already exists");
            emailEditText.requestFocus();
            return;
        }

        if (dbHelper.addUser(email, firstName, lastName, password, gender, city, phone)) {
            showSuccessAnimation();
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            intent.putExtra("registered_email", email);
            startActivity(intent);
            finish();
        } else {
            showErrorAnimation();
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInput(String email, String firstName, String lastName,
                                  String password, String confirmPassword, String phone,
                                  String gender, String city) {

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            firstNameEditText.setError("First name must be at least 3 characters");
            firstNameEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            lastNameEditText.setError("Last name must be at least 3 characters");
            lastNameEditText.requestFocus();
            return false;
        }

        if (!isValidPassword(password)) {
            passwordEditText.setError("Password must be at least 5 characters with 1 letter, 1 number, and 1 special character");
            passwordEditText.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        if (gender.equals("Select Gender")) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (city.equals("Select City")) {
            Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            phoneEditText.setError("Please enter a valid phone number");
            phoneEditText.requestFocus();
            return false;
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

    private void showSuccessAnimation() {
        Animation successAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        registerButton.startAnimation(successAnimation);
    }

    private void showErrorAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        registerButton.startAnimation(shake);
    }
}