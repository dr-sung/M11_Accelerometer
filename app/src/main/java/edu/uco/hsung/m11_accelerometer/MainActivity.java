package edu.uco.hsung.m11_accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final int UPDATE_THRESHOLD = 500;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView xView, yView, zView, allView;
    private long lastUpdate;
    private Listener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xView = (TextView) findViewById(R.id.x_value_view);
        yView = (TextView) findViewById(R.id.y_value_view);
        zView = (TextView) findViewById(R.id.z_value_view);
        allView = (TextView) findViewById(R.id.all_sensors);

        listener = new Listener(); // accelerometer listener

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            xView.setText("No Accelerometer available on this device!");
        }

        showAllSensors();

    }

    // Register listener
    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);

        lastUpdate = System.currentTimeMillis();

    }

    // Unregister listener
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(listener);
        super.onPause();
    }

    class Listener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long actualTime = System.currentTimeMillis();
                if (actualTime - lastUpdate > UPDATE_THRESHOLD) {
                    lastUpdate = actualTime;
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    xView.setText("" + x);
                    yView.setText("" + y);
                    zView.setText("" + z);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void showAllSensors() {
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String m = "";
        for (Sensor s : deviceSensors) {
            m += s.getName() + "\n";
        }
        allView.setText(m);
    }
}