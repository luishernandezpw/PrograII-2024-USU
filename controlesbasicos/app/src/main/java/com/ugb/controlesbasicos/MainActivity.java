package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempVal = findViewById(R.id.lblAcelerometro);
        activarSensorAcelerometro();
    }
    @Override
    protected void onResume() {
        iniciarAclerometro();
        super.onResume();
    }
    @Override
    protected void onPause() {
        detenerAcelerometro();
        super.onPause();
    }
    private void activarSensorAcelerometro(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor==null){
            tempVal.setText("Tu telefono NO tiene sensor Acelerometro.");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                tempVal.setText("Acelerometro: X="+ sensorEvent.values[0] +"; Y="+ sensorEvent.values[1] +"; Z="+ sensorEvent.values[2]);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    private void iniciarAclerometro(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }
    private void detenerAcelerometro(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}