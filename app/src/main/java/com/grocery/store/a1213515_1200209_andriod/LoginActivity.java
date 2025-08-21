package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private TextView signUpTextView;
    private DatabaseHelper dbHelper;
    private SharedPreferencesManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupDatabase();
        loadRememberedEmail();
        setupAnimations();
        setupClickListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.sign_up_text);

        prefsManager = new SharedPreferencesManager(this);
    }

    private void setupDatabase() {
        dbHelper = new DatabaseHelper(this);
    }

    private void loadRememberedEmail() {
        String rememberedEmail = prefsManager.getRememberedEmail();
        if (!rememberedEmail.isEmpty()) {
            emailEditText.setText(rememberedEmail);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void setupAnimations() {
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        findViewById(R.id.login_container).startAnimation(fadeIn);
        emailEditText.startAnimation(slideInLeft);
        passwordEditText.startAnimation(slideInRight);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        signUpTextView.setOnClickListener(v -> openRegistration());
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        // Check for admin first using isUserAdmin method
        if (dbHelper.isUserAdmin(email, password)) {
            handleSuccessfulLogin(email, true);
            return;
        }

        // Then check regular users in database
        if (dbHelper.checkUser(email, password)) {
            handleSuccessfulLogin(email, false);
        } else {
            showLoginError();
        }
    }

    private boolean validateInput(String email, String password) {
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

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void handleSuccessfulLogin(String email, boolean isAdmin) {
        if (rememberMeCheckBox.isChecked()) {
            prefsManager.saveRememberedEmail(email);
        } else {
            prefsManager.clearRememberedEmail();
        }

        prefsManager.saveLoginSession(email, isAdmin);

        Animation successAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        loginButton.startAnimation(successAnimation);

        if (isAdmin) {
            Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        finish();
    }

    private void showLoginError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        loginButton.startAnimation(shake);
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
    }

    private void openRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}