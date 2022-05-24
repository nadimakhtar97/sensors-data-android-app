package com.example.sensorsdata

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SensorDataService : Service() {

    private val TAG = "SensorDataService"
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var mHeartrate: Sensor
    private lateinit var sensorData: SensorData
    private val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
    private lateinit var file: File
//    private lateinit var liveData : LiveData<SensorData>
    private val CHANNEL_ID = "1001"

    companion object {
        val liveData = MutableLiveData<SensorData>()
    }




    @SuppressLint("SimpleDateFormat")
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        Log.d(TAG, "onCreate: service created")
        val pendingIntent: PendingIntent =
            Intent(this, SensorDataActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1,notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d(TAG, "onStartCommand: start service")
        createFile(intent?.getStringExtra(FILE_NAME))
        sensorData = SensorData(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        liveData.value = sensorData

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


        mSensorManager.registerListener(
            mSensorEventListener,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )

        mSensorManager.registerListener(
            mSensorEventListener,
            mGyroscope,
            SensorManager.SENSOR_DELAY_GAME
        )

        mSensorManager.registerListener(
            mSensorEventListener,
            mHeartrate,
            SensorManager.SENSOR_DELAY_GAME
        )



        return START_STICKY
    }

    // Handling sensor event listener
    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
//            Log.d(TAG, "onSensorChanged: service running in background ${sdf.format(Date())}")
            when (event.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    sensorData.accX = event.values[0]
                    sensorData.accY = event.values[1]
                    sensorData.accZ = event.values[2]
                    file.appendText(
                        "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                                "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                                "${sensorData.bpm} \n"
                    )
                    liveData.value = sensorData
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
                }
                Sensor.TYPE_GYROSCOPE -> {
                    sensorData.gyroX = event.values[0]
                    sensorData.gyroY = event.values[1]
                    sensorData.gyroZ = event.values[2]
                    file.appendText(
                        "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                                "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                                "${sensorData.bpm} \n"
                    )
                    liveData.value = sensorData
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
                }
                Sensor.TYPE_HEART_RATE -> {
//                    Log.d(TAG, "onSensorChanged: Hear Rate Sensor")
                    sensorData.bpm = event.values[0]
                    file.appendText(
                        "${sdf.format(Date())}, ${sensorData.accX},${sensorData.accY},${sensorData.accZ}," +
                                "${sensorData.gyroX},${sensorData.gyroY},${sensorData.gyroZ}," +
                                "${sensorData.bpm} \n"
                    )
                    liveData.value = sensorData
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
        }

        override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}
    }

    private fun createFile(fileName: String?) {
        val currentDateAndTime = sdf.format(Date())
        file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "$fileName ${currentDateAndTime}.csv"
        )

        file.appendText(
            """
            Date&Time           aX          aY          aZ          gX         gY          gZ          hr
            ----------------------------------------------------------------------------------------------
            
        """.trimIndent()
        )
//        Log.i(TAG, "onCreate: ${this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: stop service")


        stopForeground(true)
        stopSelf()
        super.onDestroy()
        sensorData.accX = 0f
        sensorData.accY = 0f
        sensorData.accZ = 0f
        sensorData.gyroX = 0f
        sensorData.gyroY = 0f
        sensorData.gyroZ = 0f
        sensorData.bpm = 0f
        liveData.value = sensorData
        mSensorManager.unregisterListener(mSensorEventListener)

    }

    private fun createChannel(){
        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name : CharSequence = getString(R.string.app_name)
        val description = "Sensor data foreground service notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID,name,importance)
        mChannel.description = description
        mChannel.setShowBadge(false)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        mNotificationManager.createNotificationChannel(mChannel)

    }

}