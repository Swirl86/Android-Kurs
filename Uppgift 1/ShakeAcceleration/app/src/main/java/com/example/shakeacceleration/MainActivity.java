package com.example.shakeacceleration;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

public class MainActivity extends AppCompatActivity  {

    private final float SHAKE_THRESHOLD = 1.2f;
    private final int SHAKE_TIMEOUT_MS = 500;

    private MainFragment accelerometer;
    private FragmentContainerView  fragmentContainerView;
    private TextView textView;
    private long rotatedTimeStamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerometer = new MainFragment(this);
        fragmentContainerView = (FragmentContainerView) findViewById(R.id.id_fragment_container_view);
        textView = (TextView) findViewById(R.id.id_info_text_view);

        accelerometer.setListener(new MainFragment.Listener() {
            @Override
            public void onTranslation(float x, float y, float z) {
                double gForce = getForce(x, y, z);
                long  currentTime = System.currentTimeMillis();
                textView.setBackgroundColor(Color.RED);
                textView.setText("X: " + x + "\nY: " + y + "\nZ: " + z + "\nG-force: " + gForce);

                if(gForce > SHAKE_THRESHOLD){
                    if(rotatedTimeStamp + SHAKE_TIMEOUT_MS < currentTime) {
                        textView.setBackgroundColor(Color.GREEN);
                        fragmentContainerView.setRotation(fragmentContainerView.getRotation() + 90);
                        rotatedTimeStamp = currentTime;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer.register();
    }

    @Override
    protected void onPause() {
        accelerometer.unregister();
        super.onPause();
    }

    private double getForce(double x, double y, double z) {
        double gX = x / SensorManager.GRAVITY_EARTH;
        double gY = y / SensorManager.GRAVITY_EARTH;
        double gZ = z / SensorManager.GRAVITY_EARTH;
        return Math.sqrt((double) (gX * gX + gY * gY + gZ * gZ));
    }
}