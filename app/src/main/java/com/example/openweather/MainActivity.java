package com.example.openweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private TextView tvCityName, tvTemperature, tvHumidity, tvDescription;
    private ImageView imgCity;
    private String API_KEY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        tvCityName = findViewById(R.id.tvCityName);
        tvHumidity = findViewById(R.id.tvCityHumidity);
        tvTemperature = findViewById(R.id.tvCityTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        imgCity = findViewById(R.id.imgCityWeather);

    }
    private String buildUrl(String city){
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi", city, API_KEY);
    }

    private String buildImageUrl(String icon){
        return String.format("http://openweathermap.org/img/wn/%s@4x.png", icon);
    }

    public void getWeather(View view) {
        // Buoc 1: Lay du lieu tu edittext => biet city
        String city = edtCity.getText().toString();
        if(city.isEmpty()){
            edtCity.setError("City name cant empty!");
            return;
        }
        // Buoc 2: Tao url va gui request openweather
        String url = buildUrl(city);
        Log.d("url", url);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Thanh cong
                        try {
                            // Nhiet do
                            double temperature = response.getJSONObject("main").getDouble("temp");
                            tvTemperature.setText("Temperature: "+ temperature);
                            // Do am
                            double humidity = response.getJSONObject("main").getDouble("humidity");
                            tvHumidity.setText("Humidity: "+ humidity);
                            // Mo ta
                            String description = response.getJSONArray("weather").getJSONObject(0).getString("description");
                            tvDescription.setText(description);
                            // Hinh anh
                            String icon = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String imageUrl = buildImageUrl(icon);
                            Picasso.get().load(imageUrl).into(imgCity);

                            }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "API Error", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);

        // Buoc 3: Lay du lieu -> boc tach du lieu -> hien thi view


        // Buoc 4: Kiem tra
    }
}