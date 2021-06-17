package com.example.weatherservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherListActivity extends AppCompatActivity {

    private RecyclerView weatherView;
    private WeatherAdapter weatherAdapter;

    private TextView locationValue, weatherValue;
    private ImageView imageWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);
    }
}