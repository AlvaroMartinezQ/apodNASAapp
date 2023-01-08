package com.urjc.nasaapod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class APOD extends AppCompatActivity {

    // APOD = Astrology Pic Of the Day

    private ImageView nasaImage;
    private TextView loading;
    private TextView explanation;
    private String imageUrl;
    private Gson gson;

    private String titleStored;
    private String explanationStored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        nasaImage = findViewById(R.id.apodImage);
        loading = findViewById(R.id.loading);
        explanation = findViewById(R.id.explanation);
        explanation.setMovementMethod(new ScrollingMovementMethod());

        gson = new Gson();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String date = extras.getString("date");
            request(date);
        } else {
            if (imageUrl == null) {
                request("");
            } else {
                loadImage();
            }
        }
    }

    private void request(String date) {
        try {
            Request req;
            if (date == "") {
                req = new Request.Builder().url(APIEndpoints.apodDefault).build();
            } else {
                req = new Request.Builder().url(APIEndpoints.apodDate + date).build();
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);

            HTTPClient.client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i("Error", "Error on request: " + e);
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        APIResponse res = gson.fromJson(response.body().string(), APIResponse.class);
                        imageUrl = res.url;
                        titleStored = res.title;
                        explanationStored = res.explanation;
                        explanation.setText(explanationStored);
                        loading.setText(titleStored);
                    }
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            if (imageUrl != null) {
                loadImage();
            } else {
                loading.setText(R.string.error);
            }
        } catch (InterruptedException e) {
            loading.setText(R.string.error);
        }
    }

    private void loadImage() {
        Glide.with(this).load(imageUrl).into(nasaImage);
        loading.setText(R.string.apod);
        explanation.setText(explanationStored);
        loading.setText(titleStored);
    }

}