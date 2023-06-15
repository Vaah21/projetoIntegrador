package com.example.jogo1;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.SensorManager;

public interface Sensor {
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    static Object getSystemService(String sensorService) {
        return null;
    }
}

//adicionar métodos onCreate() -> SensorManager
//SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//onResume() -> Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

//onSensorChanged -> public void onSensorChanged(SensorEvent event) {
//    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//        float x = event.values[0];
//        float y = event.values[1];
//
//    }
//}

//onPause -> sensorManager.unregisterListener(this);
