package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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

        tempVal = findViewById(R.id.lblLuz);
        activarSensorLuz();
    }
    @Override
    protected void onResume() {
        iniciarLuz();
        super.onResume();
    }
    @Override
    protected void onPause() {
        detenerLuz();
        super.onPause();
    }
    private void activarSensorLuz(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor==null){
            tempVal.setText("Tu telefono NO tiene sensor Luz.");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                double valor = sensorEvent.values[0];
                tempVal.setText("Luz: "+ valor);
                if( valor<=20 ){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (valor<=50) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }else{
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    private void iniciarLuz(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }
    private void detenerLuz(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}