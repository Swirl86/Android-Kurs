package com.example.weatherservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
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

public class BootReceiver extends BroadcastReceiver {

    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String key = "099eff339f56d6a29a9823857b2f2671";
    private DBHelper dBhelper;

    private final String celsius = "°C";

    private String iconUrl, weather, location;


    @Override
    public void onReceive(Context context, Intent intent) {
        dBhelper = new DBHelper(context);

        location = dBhelper.getLatestEntry();

        if(location.equals("")){
            location = "Ronneby";
        }

        // This callback is fully executed even if the app is closed or open
        Intent aIntent = new Intent(context, MainActivity.class);
        aIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(aIntent);

        String getURL = url + location + "&units=metric&APPID=" + key + "&mode=json";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest JsonRequest = new JsonObjectRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String timeStamp = getTimeStamp();

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject countryJSONObject = response.getJSONObject("sys");
                            JSONObject temperatureJSONObject = response.getJSONObject("main");

                            setWeatherValues(weatherArray);

                            String country = countryJSONObject.getString("country");
                            String temperature = temperatureJSONObject.getInt("temp") + celsius;
                            String temperatureMin = temperatureJSONObject.getInt("temp_min") + celsius;
                            String temperatureMax = temperatureJSONObject.getInt("temp_max") + celsius;

                            // Create table entry
                            WeatherEntity details = new WeatherEntity(location, country,
                                    timeStamp, weather, temperature, temperatureMin,
                                    temperatureMax, iconUrl);
                            dBhelper.insertWeatherDetails(details);

                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(2000);
                            Toast.makeText(context, location + " updated:\n" + timeStamp, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "onErrorResponse: " + error);
            }
        });
        queue.add(JsonRequest);
    }

    public void setWeatherValues(JSONArray weatherArray) throws JSONException {
        String icon = "";
        for (int i = 0; i < weatherArray.length(); i++) {
            JSONObject weatherJSONObject = weatherArray.getJSONObject(i);
            weather = weatherJSONObject.getString("description");
            if (!weather.equals("")) {
                weather = weather.substring(0, 1).toUpperCase() + weather.substring(1);
            }
            icon = weatherJSONObject.getString("icon");
            iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";
        }
    }

    public String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

}