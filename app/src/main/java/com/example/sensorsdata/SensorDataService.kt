package com.example.sensorsdata

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SensorDataService : Service(){

    private val TAG = "SensorDataService"
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var mHeartrate: Sensor
    private lateinit var sensorData: SensorData
    private val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
    private lateinit var file: File


    @SuppressLint("SimpleDateFormat")
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorData = SensorData(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        } else {
            Toast.makeText(
                this,
                "No accelerometer sensor available on your device",
                Toast.LENGTH_LONG
            ).show()
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        } else {
            Toast.makeText(this, "No gyroscope sensor available on your device", Toast.LENGTH_LONG)
                .show()

        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
            mHeartrate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
//            Log.d(TAG, "onCreate: $mHeartrate")
//            Toast.makeText(this, "heart rate sensor is available", Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(this, "No hear rate sensor available on your device", Toast.LENGTH_LONG)
                .show()

        }
    }

    // Handling sensor event listener
    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                sensorData.accX = event.values[0]
                sensorData.accY = event.values[1]
                sensorData.accZ = event.values[2]
                file.appendText(
                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                            "${sensorData.bpm} \n"
                )
//                val x = String.format("%.2f", event.values[0])
//                val y = String.format("%.2f", event.values[1])
//                val z = String.format("%.2f", event.values[2])
//                binding.ax.text = x
//                binding.ay.text = y
//                binding.az.text = z
//                Log.d(
//                    TAG, """
//                    onSensorChanged:
//                    X = $x
//                    Y = $y
//                    Z = $z
//                """.trimIndent()
//                )
            } else if (event.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                sensorData.gyroX = event.values[0]
                sensorData.gyroY = event.values[1]
                sensorData.gyroZ = event.values[2]
                file.appendText(
                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                            "${sensorData.bpm} \n"
                )
//                val x = String.format("%.2f", event.values[0])
//                val y = String.format("%.2f", event.values[1])
//                val z = String.format("%.2f", event.values[2])
//                binding.gx.text = x
//                binding.gy.text = y
//                binding.gz.text = z
//                Log.d(
//                    TAG, """
//                    onSensorChanged this is gyroscope data:
//                    X = $x
//                    Y = $y
//                    Z = $z
//                """.trimIndent()
//                )
            } else if (event.sensor?.type == Sensor.TYPE_HEART_RATE) {
                Log.d(TAG, "onSensorChanged: Hear Rate Sensor")
                sensorData.bpm = event.values[0]
                file.appendText(
                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                            "${sensorData.bpm} \n"
                )
//                val bpm = String.format("%.1f", event.values[0])
//                binding.hr.text = bpm
//                Log.d(
//                    TAG, """
//                    onSensorChanged this is heart rate data:
//                   $bpm
//                """.trimIndent()
//                )
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
    }

}