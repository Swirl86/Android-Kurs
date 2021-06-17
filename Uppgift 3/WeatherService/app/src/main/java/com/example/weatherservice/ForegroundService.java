package com.example.weatherservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForegroundService extends Service {

    public static final String NOTIFY_ID = "IDChannel";

    public ForegroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Bundle editBundle = intent.getBundleExtra("weatherInfo");
        String location = editBundle.getString("location");
        String weather = editBundle.getString("weather");
        String iconUrl = editBundle.getString("iconUrl");


        Notification notification = null;
        // TODO get bitmap to work -_-
        Bitmap bitmap = getBitmapFromURL(iconUrl);

      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
          /*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
          String formattedDate = "";
          formattedDate = sdf.format(new Date());*/

            notification = new Notification.Builder(this, NOTIFY_ID)
                    //.setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setContentTitle(location)
                    .setContentText(weather)
                    .setSmallIcon(R.drawable.ic_baseline_wb_sunny_24)
                    .setLargeIcon(bitmap)
                    .build();

            startForeground(5, notification);
       }

        return super.onStartCommand(intent, flags, startId);
    }

    Bitmap bitmap;

    public Bitmap getBitmapFromURL(String src) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;

                try {
                    url = new URL(src);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    System.out.println(input.toString());
                    bitmap = BitmapFactory.decodeStream(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmap = null;
                }
            }
        }).start();
        System.out.println("BITMAP " + bitmap);
        return bitmap;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIFY_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}