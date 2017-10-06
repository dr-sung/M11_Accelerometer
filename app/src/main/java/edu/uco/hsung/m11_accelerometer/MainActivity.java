package edu.uco.hsung.m11_accelerometer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView xView;
    private TextView yView;
    private TextView zView;
    private Button allSensors;
    private Listener listener;

    private final float ALPHA = 0.8f;
    private final long UPDATE_INTERVAL = 500; // 500ms

    private float[] current = new float[3];
    private float[] constant_value = new float[3];
    private float[] acceleration = new float[3];

    private long lastUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xView = findViewById(R.id.x_tv);
        yView = findViewById(R.id.y_tv);
        zView = findViewById(R.id.z_tv);

        allSensors = findViewById(R.id.show_sensors);

        listener = new Listener(); // accelerometer listener

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            xView.setText(R.string.no_accelerometer);
        }

        allSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllSensors.class);
                startActivity(intent);
            }
        });

        lastUpdate = System.currentTimeMillis();


    }

    // Register listener
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    // Unregister listener
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(listener);
        super.onPause();
    }

    private class Listener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastUpdate < UPDATE_INTERVAL) {
                    return; // update only every 0.5 sec
                }

                lastUpdate = currentTime;

                for (int i = 0; i < event.values.length; i++) {
                    current[i] = event.values[i];
                }

                lowPassFilter(current, constant_value);
                highPassFilter(current, constant_value, acceleration);

                Resources res = getResources();
                xView.setText(String.format(res.getString(R.string.x_values),
                        current[0], constant_value[0], acceleration[0]));
                yView.setText(String.format(res.getString(R.string.y_values),
                        current[1], constant_value[1], acceleration[1]));
                zView.setText(String.format(res.getString(R.string.z_values),
                        current[2], constant_value[2], acceleration[2]));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    // suppress transient forces (high frequency factors)
    private void lowPassFilter(float[] input, float[] output) {
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
    }

    // suppress constance forces (low frequency factors)
    private void highPassFilter(float[] input, float[] constant_val, float[] output) {
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i] - constant_val[i];
        }
    }

}