package com.example.weatherservice;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {


    ArrayList<WeatherEntity> localWeatherEntitys;

    // Every List item
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView locationValue, weatherValue;
        private ImageView imageWeather;

        public ViewHolder(View itemView) {
            super(itemView);

            locationValue = itemView.findViewById(R.id.locationTitle);
            weatherValue = itemView.findViewById(R.id.weatherValue);
            imageWeather = itemView.findViewById(R.id.imageWeather);
        }

        public TextView getLocationValue() {
            return locationValue;
        }

        public TextView getWeatherValue() {
            return weatherValue;
        }

        public ImageView getImageWeather() {
            return imageWeather;
        }
    }

    public WeatherAdapter(ArrayList<WeatherEntity> weatherEntitys) {
        this.localWeatherEntitys = weatherEntitys;
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
        // Match function with ui parts

        Log.d("ADA", "onBindViewHolder: " + localWeatherEntitys.get(position).getLocation());

        holder.getLocationValue().setText(localWeatherEntitys.get(position).getLocation());
        holder.getWeatherValue().setText(localWeatherEntitys.get(position).getCountry());
       // holder.getImageWeather().setImageIcon(localWeatherEntitys.get(position).getWeatherImage());

    }

    @Override
    public int getItemCount() {
        return localWeatherEntitys.size();
    }
}
