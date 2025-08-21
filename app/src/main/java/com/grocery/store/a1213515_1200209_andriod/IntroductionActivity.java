package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntroductionActivity extends AppCompatActivity {

    private Button connectButton;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        initViews();
        setupRetrofit();
        setupAnimations();
    }

    private void initViews() {
        connectButton = findViewById(R.id.connect_button);
        progressBar = findViewById(R.id.progress_bar);

        connectButton.setOnClickListener(v -> checkConnection());
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void setupAnimations() {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        findViewById(R.id.intro_content).startAnimation(slideUp);
    }

    private void checkConnection() {
        connectButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ProductResponse> call = apiService.getGroceryProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.GONE);
                connectButton.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getProducts() != null && !response.body().getProducts().isEmpty()) {

                    Toast.makeText(IntroductionActivity.this, "Connected! Found " +
                            response.body().getProducts().size() + " grocery items", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showConnectionError();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                connectButton.setVisibility(View.VISIBLE);
                showConnectionError();
            }
        });
    }

    private void showConnectionError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        connectButton.startAnimation(shake);
        Toast.makeText(this, "Connection failed. Please check your internet connection.",
                Toast.LENGTH_LONG).show();
    }
}