package com.example.basicexaple.retrofit_example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.basicexaple.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitExample extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_example);
        listView = findViewById(R.id.listViewHeroes);
        getDetailed();
    }

    private void getDetailed() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitClient client = retrofit.create(RetrofitClient.class);

        Call<List<Model>> call = client.getDetails();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Model>> call, @NonNull Response<List<Model>> response) {
                List<Model> list = response.body();
                assert list != null;
                String[] listData = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    listData[i] = list.get(i).getName();
                }
                listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listData));
            }

            @Override
            public void onFailure(@NonNull Call<List<Model>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
