package com.example.sensorsdata

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import com.example.sensorsdata.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.example.sensorsdata.SensorData
import com.example.sensorsdata.databinding.ActivitySensorDataBinding
import com.example.sensorsdata.SensorDataActivity


class SensorDataActivity : AppCompatActivity() {

    private val TAG = "SensorDataActivity"
    private lateinit var binding: ActivitySensorDataBinding
//    private lateinit var mSensorManager: SensorManager
//    private lateinit var mAccelerometer: Sensor
//    private lateinit var mGyroscope: Sensor
//    private lateinit var mHeartrate: Sensor
//    private lateinit var sensorData: SensorData
    //    var dir = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Documents")
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
//    private var isReadPermissionGranted = false
//    private var isWritePermissionGranted = false
    private var isBodySensorGranted = false
//    private lateinit var file: File
    private var start = 1
//    private val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.toolbar_title_layout)
        binding = ActivitySensorDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//                isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
//                    ?: isReadPermissionGranted
//                isWritePermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
//                    ?: isWritePermissionGranted
                isBodySensorGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                    ?: isBodySensorGranted
            }
        requestPermission()
//        askForPermissions()

//        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
//            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//        } else {
//            Toast.makeText(
//                this,
//                "No accelerometer sensor available on your device",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
//            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
//        } else {
//            Toast.makeText(this, "No gyroscope sensor available on your device", Toast.LENGTH_LONG)
//                .show()
//
//        }
//
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
//            mHeartrate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
//            Log.d(TAG, "onCreate: $mHeartrate")
//            Toast.makeText(this, "heart rate sensor is available", Toast.LENGTH_LONG).show()
//
//        } else {
//            Toast.makeText(this, "No hear rate sensor available on your device", Toast.LENGTH_LONG)
//                .show()
//
//        }

        // Handle Start and Stop button click listener
        binding.button.setOnClickListener {
            if (start == 1) {
//                createFile()
//                mSensorManager.registerListener(
//                    mSensorEventListener,
//                    mAccelerometer,
//                    SensorManager.SENSOR_DELAY_GAME
//                )
//
//                mSensorManager.registerListener(
//                    mSensorEventListener,
//                    mGyroscope,
//                    SensorManager.SENSOR_DELAY_GAME
//                )
//
//                mSensorManager.registerListener(
//                    mSensorEventListener,
//                    mHeartrate,
//                    SensorManager.SENSOR_DELAY_GAME
//                )

                start = 0;
                binding.button.text = "STOP"
            } else {
                start = 1;
                binding.button.text = "START"
//                mSensorManager.unregisterListener(mSensorEventListener)
                binding.ax.text = "0"
                binding.ay.text = "0"
                binding.az.text = "0"
                binding.gx.text = "0"
                binding.gy.text = "0"
                binding.gz.text = "0"
                binding.hr.text = "0"
            }

        }

    }

    // Handling sensor event listener
//    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
//        override fun onSensorChanged(event: SensorEvent) {
//            if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
//                sensorData.accX = event.values[0]
//                sensorData.accY = event.values[1]
//                sensorData.accZ = event.values[2]
//                file.appendText(
//                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
//                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
//                            "${sensorData.bpm} \n"
//                )
//                val x = String.format("%.2f", event.values[0])
//                val y = String.format("%.2f", event.values[1])
//                val z = String.format("%.2f", event.values[2])
//                binding.ax.text = x
//                binding.ay.text = y
//                binding.az.text = z
////                Log.d(
////                    TAG, """
////                    onSensorChanged:
////                    X = $x
////                    Y = $y
////                    Z = $z
////                """.trimIndent()
////                )
//            } else if (event.sensor?.type == Sensor.TYPE_GYROSCOPE) {
//                sensorData.gyroX = event.values[0]
//                sensorData.gyroY = event.values[1]
//                sensorData.gyroZ = event.values[2]
//                file.appendText(
//                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
//                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
//                            "${sensorData.bpm} \n"
//                )
//                val x = String.format("%.2f", event.values[0])
//                val y = String.format("%.2f", event.values[1])
//                val z = String.format("%.2f", event.values[2])
//                binding.gx.text = x
//                binding.gy.text = y
//                binding.gz.text = z
////                Log.d(
////                    TAG, """
////                    onSensorChanged this is gyroscope data:
////                    X = $x
////                    Y = $y
////                    Z = $z
////                """.trimIndent()
////                )
//            } else if (event.sensor?.type == Sensor.TYPE_HEART_RATE) {
//                Log.d(TAG, "onSensorChanged: Hear Rate Sensor")
//                sensorData.bpm = event.values[0]
//                file.appendText(
//                    "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
//                            "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
//                            "${sensorData.bpm} \n"
//                )
//                val bpm = String.format("%.1f", event.values[0])
//                binding.hr.text = bpm
//                Log.d(
//                    TAG, """
//                    onSensorChanged this is heart rate data:
//                   $bpm
//                """.trimIndent()
//                )
//            }
//        }
//
//        override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
//    }


    // request for multiple permissions
    private fun requestPermission() {

//        isReadPermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isWritePermissionGranted = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED

        isBodySensorGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED


        val permissionRequest: MutableList<String> = ArrayList()
//        if (!isReadPermissionGranted) {
//            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//        if (!isWritePermissionGranted) {
//            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
        if (!isBodySensorGranted) {
            permissionRequest.add(Manifest.permission.BODY_SENSORS)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    private fun createFile() {
        val fileName = intent.getStringExtra(KEY_1)
        val currentDateAndTime = sdf.format(Date())
        file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "$fileName ${currentDateAndTime}.csv"
        )

        file.appendText("""
            Date&Time           aX          aY          aZ          gX         gY          gZ          hr
            ----------------------------------------------------------------------------------------------
            
        """.trimIndent())
        Log.i(TAG, "onCreate: ${this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}")
    }


    // on pause life cycle
//    override fun onPause() {
//        super.onPause()
//        mSensorManager.unregisterListener(mSensorEventListener)
//    }


    // On resume life cycle
//    override fun onResume() {
//        super.onResume()
//        if (Environment.isExternalStorageManager()) {
//            createDir()
//        }
//    }

    // ask for external storage permission
//    private fun askForPermissions() {
//        Log.d(TAG, "askForPermissions: ")
//        if (!Environment.isExternalStorageManager()) {
//            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//            startActivity(intent)
//            return
//        }
//        createDir()
//    }

    // create directory if not present
//    private fun createDir() {
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }
//    }
}
