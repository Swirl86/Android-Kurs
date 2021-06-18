package com.example.weatherservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeatherListActivity extends AppCompatActivity {

    private RecyclerView weatherView;
    private WeatherAdapter weatherAdapter;

    private DBHelper dBhelper;

    private Button backBtn, deleteAllBtn;

    private TextView locationValue, weatherValue;
    private ImageView imageWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list);

        dBhelper = new DBHelper(WeatherListActivity.this);
        ArrayList<WeatherEntity> weatherEntity = new ArrayList<WeatherEntity>();

        weatherEntity=  dBhelper.getWeatherList(weatherEntity);

       /* for(WeatherEntity ent : weatherEntity) {
            Log.d("weatherEntity", "weatherEntity: " + ent.getLocation());
        }*/

        weatherView = findViewById(R.id.showValue);
        weatherView.setLayoutManager( new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(weatherEntity);

        weatherView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        weatherView.setAdapter(weatherAdapter);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherListActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        deleteAllBtn = findViewById(R.id.deleteAllBtn);
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dBhelper.deleteAll();
                Intent intent = new Intent(WeatherListActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

    }
}