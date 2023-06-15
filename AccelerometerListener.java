package com.example.jogo1;// AccelerometerListener.java
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.jogo1.GameView;

public class AccelerometerListener implements SensorEventListener {
    private GameView gameView;

    public AccelerometerListener(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Empty implementation
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = -event.values[0];  // Como utilizar corretamente o eixo x para movimentar o boneco
            gameView.updatePosition(x);
        }
    }
}
