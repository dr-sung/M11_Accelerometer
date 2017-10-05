package edu.uco.hsung.m11_accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AllSensors extends Activity {

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sensors);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> sensors = new ArrayList<>();
        for (Sensor s : deviceSensors) {
            sensors.add(s.getName());
        }

        ListView listview = findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.item_view,
                sensors);

        listview.setAdapter(adapter);
    }


}
