package com.example.weatherservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=";

    // Update openweathermap Key number below
    private final String key = "099eff339f56d6a29a9823857b2f2671";

    private final String celsius = "°C";

    private String iconUrl, weather, location;

    private EditText inputCity, inputZipCode;
    private Button submitBtn, historyBtn;
    private ImageView weatherImage;
    private TextView locationTitle, timeTitle, status, temp, tempMin, tempMax, errorMsg;

    private Context context;
    private DBHelper dBhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        setAlarm();

        submitBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);

    }

    private void initValues() {
        context = getApplicationContext();
        dBhelper = new DBHelper(context);

        inputCity = (EditText) findViewById(R.id.inputCity);
        inputZipCode = (EditText) findViewById(R.id.inputZipCode);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        historyBtn = (Button) findViewById(R.id.historyBtn);

        errorMsg = (TextView) findViewById(R.id.errorMsg);

        locationTitle = (TextView) findViewById(R.id.locationTitle);
        timeTitle = (TextView) findViewById(R.id.timeTitle);
        status = (TextView) findViewById(R.id.status);
        temp = (TextView) findViewById(R.id.temp);
        tempMin = (TextView) findViewById(R.id.tempMin);
        tempMax = (TextView) findViewById(R.id.tempMax);
        weatherImage = (ImageView) findViewById(R.id.weatherImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                onClickHandleSubmit("");
                break;
            case R.id.historyBtn:
                onClickHandleHistory(view);
                break;
            default:
                break;
        }
    }

    public void onClickHandleSubmit(String repeatFetchLoc) {
        String input = inputCity.getText().toString();

        if (input.equals("")) {
            Toast.makeText(this, "Enter City Name!", Toast.LENGTH_SHORT).show();
        } else {

            String zipCode = inputZipCode.getText().toString();
            zipCode = zipCode.equals("") ? "" : "," + zipCode;

            RequestQueue queue = Volley.newRequestQueue(context);

            String getURL = url + inputCity.getText() + zipCode + "&units=metric&APPID=" + key + "&mode=json";

            JsonObjectRequest JsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String timeStamp = getTimeStamp();
                                timeTitle.setText(timeStamp);

                                JSONArray weatherArray = response.getJSONArray("weather");
                                JSONObject countryJSONObject = response.getJSONObject("sys");
                                JSONObject temperatureJSONObject = response.getJSONObject("main");

                                setWeatherValues(weatherArray);

                                String loc = repeatFetchLoc;
                                if(loc.equals("")){
                                    loc = response.get("name").toString();
                                    location = loc.substring(0, 1).toUpperCase() + loc.substring(1);
                                }


                                String country = countryJSONObject.getString("country");
                                locationTitle.setText(location + " " + country);

                                String temperature = temperatureJSONObject.getInt("temp") + celsius;
                                String temperatureMin = temperatureJSONObject.getInt("temp_min") + celsius;
                                String temperatureMax = temperatureJSONObject.getInt("temp_max") + celsius;

                                temp.setText(temperature);
                                tempMin.setText(temperatureMin);
                                tempMax.setText(temperatureMax);


                                // Create table entry
                                WeatherEntity details = new WeatherEntity(location, country,
                                        timeStamp, weather, temperature, temperatureMin,
                                        temperatureMax, iconUrl);
                                dBhelper.insertWeatherDetails(details);

                                startService();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorMsg.setVisibility(View.VISIBLE);

                    errorMsg.postDelayed(new Runnable() {
                        public void run() {
                            errorMsg.setVisibility(View.GONE);
                        }
                    }, 5000);

                    Log.d("volley", "onErrorResponse: " + error);
                }
            });
            queue.add(JsonRequest);
        }
    }


    public void onClickHandleHistory(View view) {
        Intent intent = new Intent(this, WeatherListActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    public void setWeatherValues(JSONArray weatherArray) throws JSONException {
        String icon = "";

        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject weatherJSONObject = weatherArray.getJSONObject(i);
            weather = weatherJSONObject.getString("description");
            if (!weather.equals("")) {
                weather = weather.substring(0, 1).toUpperCase() + weather.substring(1);
            }
            status.setText(weather);

            icon = weatherJSONObject.getString("icon");
            iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";

            Picasso.get().load(iconUrl).resize(300, 300).
                    centerCrop().into(weatherImage);

        }
        // Different image types
        // iconUrl = "https://openweathermap.org/img/wn/" + icon + "@4x.png";
    }

    public String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public void startService() {

        Bundle bundle = new Bundle();

        bundle.putString("location", location);
        bundle.putString("weather", weather);
        bundle.putString("iconUrl", iconUrl);

        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("weatherInfo", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Needed?
        ContextCompat.startForegroundService(this, intent);

    }


    public void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, BootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long thirtySecondsFromNow = System.currentTimeMillis() + 30 * 1000;
        alarmManager.set(AlarmManager.RTC_WAKEUP, thirtySecondsFromNow , pendingIntent);
    }
}