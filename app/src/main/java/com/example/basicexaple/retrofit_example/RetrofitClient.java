package com.example.basicexaple.retrofit_example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitClient {

    String BASE_URL = "https://simplifiedcoding.net/demos/";

    @GET("marvel")
    Call<List<Model>> getDetails();
}
