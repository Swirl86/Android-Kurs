package com.example.weatherservice;

public class WeatherEntity {

    private String location;
    private String country;
    private String time;
    private String status;
    private String temp;
    private String tempMin;
    private String tempMax;
    private String weatherImage;

    public WeatherEntity() {

        this.location = "location";
        this.country = "country";
        this.time = "time";
        this.status = "status";
        this.temp = "temperature";
        this.tempMin = "tempMin";
        this.tempMax = "tempMax";
        this.weatherImage = "image";
    }

    public WeatherEntity(String location, String country, String time, String status, String temp, String tempMin,
                         String tempMax, String weatherImage) {

        this.location = location;
        this.country = country;
        this.time = time;
        this.status = status;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.weatherImage = weatherImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getWeatherImage() {
        return weatherImage;
    }

    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }
}
