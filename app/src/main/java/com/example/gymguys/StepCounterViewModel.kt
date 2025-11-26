package com.example.gymguys

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class StepCounterViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val AVERAGE_STRIDE_LENGTH_METERS = 0.762

    private val sensorManager: SensorManager by lazy {
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val stepCounterSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private var initialSteps = -1L

    private var lastSensorSteps = -1L

    val steps = mutableStateOf(0L)
    val distanceInMeters = mutableStateOf(0.0)


    fun startListening() {
        if (stepCounterSensor == null) {
            return
        }
        sensorManager.registerListener(
            this,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }


    fun resetSteps() {
        if (lastSensorSteps != -1L) {
            initialSteps = lastSensorSteps
        }
        steps.value = 0L
        distanceInMeters.value = 0.0
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSensorSteps = event.values[0].toLong()


            lastSensorSteps = totalSensorSteps

            if (initialSteps == -1L) {
                initialSteps = totalSensorSteps
            }

            val currentSessionSteps = totalSensorSteps - initialSteps
            steps.value = currentSessionSteps


            distanceInMeters.value = currentSessionSteps * AVERAGE_STRIDE_LENGTH_METERS
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}