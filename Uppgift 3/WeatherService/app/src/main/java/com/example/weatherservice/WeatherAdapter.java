package com.example.weatherservice;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {


    ArrayList<WeatherEntity> localWeatherEntitys;

    // Every List item
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView locationValue, countryValue, weatherValue;
        private ImageView imageWeather;

        public ViewHolder(View itemView) {
            super(itemView);

            locationValue = itemView.findViewById(R.id.locationValue);
            countryValue = itemView.findViewById(R.id.countryValue);
            weatherValue = itemView.findViewById(R.id.weatherValue);
            imageWeather = itemView.findViewById(R.id.imageWeather);
        }

        public TextView getLocationValue() {
            return locationValue;
        }

        public TextView getCountryValue() {
            return countryValue;
        }

        public TextView getWeatherValue() {
            return weatherValue;
        }

        public ImageView getImageWeather() {
            return imageWeather;
        }
    }

    public WeatherAdapter(ArrayList<WeatherEntity> weatherEntitys) {
        localWeatherEntitys = weatherEntitys;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Fill out the viewGroup
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_layout,
                viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {

        /*Log.d("position", "onBindViewHolder: " + localWeatherEntitys.get(position).getLocation());
        Log.d("ViewHolder", "onBindViewHolder: " + localWeatherEntitys.get(position).getCountry());
        Log.d("ViewHolder", "onBindViewHolder: " + localWeatherEntitys.get(position).getStatus());
        Log.d("ViewHolder", "onBindViewHolder: " + localWeatherEntitys.get(position).getWeatherImage());*/

        //Log.d("position", "position: " + position);
        holder.getLocationValue().setText(localWeatherEntitys.get(position).getLocation());
        holder.getCountryValue().setText(localWeatherEntitys.get(position).getCountry());
        holder.getWeatherValue().setText(localWeatherEntitys.get(position).getStatus());
        ImageView weatherImage = holder.getImageWeather();
        Picasso.get().load(localWeatherEntitys.get(position).getWeatherImage()).resize(200, 200).
                centerCrop().into(weatherImage);
    }

    @Override
    public int getItemCount() {
       // Log.d("ADA", "getItemCount: " + localWeatherEntitys.size());
        return localWeatherEntitys.size();
    }
}
