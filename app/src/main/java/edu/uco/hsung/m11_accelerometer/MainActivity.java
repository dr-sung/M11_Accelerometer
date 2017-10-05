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

    private TextView xView, yView, zView;
    private Button allSensors;
    private Listener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xView = findViewById(R.id.x_view);
        yView = findViewById(R.id.y_view);
        zView = findViewById(R.id.z_view);
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
                Resources res = getResources();
                xView.setText(String.format(res.getString(R.string.x_message), event.values[0]));
                yView.setText(String.format(res.getString(R.string.y_message), event.values[1]));
                zView.setText(String.format(res.getString(R.string.z_message), event.values[2]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

}