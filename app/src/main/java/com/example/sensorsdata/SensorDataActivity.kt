package com.example.sensorsdata

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sensorsdata.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.example.sensorsdata.SensorData
import com.example.sensorsdata.databinding.ActivitySensorDataBinding
import com.example.sensorsdata.SensorDataActivity
import java.lang.Exception


const val FILE_NAME = "FILE_NAME"

class SensorDataActivity : AppCompatActivity() {

    private val TAG = "SensorDataActivity"
    private lateinit var binding: ActivitySensorDataBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isBodySensorGranted = false
    private var start = 1
    private lateinit var sensorDataObserver: Observer


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.toolbar_title_layout)
        binding = ActivitySensorDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isBodySensorGranted = permissions[Manifest.permission.BODY_SENSORS]
                    ?: isBodySensorGranted
            }

        requestPermission()


        // Handle Start and Stop button click listener
        binding.button.setOnClickListener {
            if (start == 1) {
                val fileName = intent?.getStringExtra(KEY_1)
                start = 0
                binding.button.text = "STOP"
//                Log.d(TAG, "onCreate: $fileName")
                startService(Intent(this,SensorDataService::class.java).apply{
                    putExtra(FILE_NAME,fileName)
                })
                setObserver()

            } else {
                stopService(Intent(this, SensorDataService::class.java))
//                binding.ax.text = "0"
//                binding.ay.text = "0"
//                binding.az.text = "0"
//                binding.gx.text = "0"
//                binding.gy.text = "0"
//                binding.gz.text = "0"
//                binding.hr.text = "0"
                binding.button.text = "START"
                start = 1
            }

        }

    }

    // request for multiple permissions
    private fun requestPermission() {

        isBodySensorGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()

        if (!isBodySensorGranted) {
            permissionRequest.add(Manifest.permission.BODY_SENSORS)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    private fun setObserver(){
        SensorDataService.liveData.observe(this) { sensorData ->
            binding.ax.text = String.format("%.2f", sensorData.accX)
            binding.ay.text = String.format("%.2f", sensorData.accY)
            binding.az.text = String.format("%.2f", sensorData.accZ)
            binding.gx.text = String.format("%.2f", sensorData.gyroX)
            binding.gy.text = String.format("%.2f", sensorData.gyroY)
            binding.gz.text = String.format("%.2f", sensorData.gyroZ)
            binding.hr.text = String.format("%.2f", sensorData.bpm)
        }
    }


}
