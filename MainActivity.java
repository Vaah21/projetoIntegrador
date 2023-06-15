package com.example.jogo1;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    private AccelerometerListener accelerometerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        gameView = new GameView(this);
        accelerometerListener = new AccelerometerListener(gameView);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager != null) {
            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (accelerometerSensor != null) {
                sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Toast.makeText(this, "Accelerometro não encontrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "SensorManager não está disponível neste dispositivo", Toast.LENGTH_SHORT).show();
        }

        setContentView(gameView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sem necessidade de uso no caso de vocês
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Sem necessidade de uso no caso de vocês
    }
}
