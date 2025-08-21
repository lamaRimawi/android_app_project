package com.grocery.store.a1213515_1200209_andriod;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products/category/groceries")
    Call<ProductResponse> getGroceryProducts();
}