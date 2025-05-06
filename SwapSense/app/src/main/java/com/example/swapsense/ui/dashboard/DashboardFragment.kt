package com.example.swapsense.ui.dashboard

import android.Manifest
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.swapsense.databinding.FragmentDashboardBinding
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.widget.Toast

class DashboardFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // TextViews to display sensor data
    private lateinit var stepCountText: TextView
    private lateinit var accelerometerText: TextView
    private lateinit var proximityText: TextView // TextView for proximity sensor

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var proximitySensor: Sensor? = null // Proximity sensor

    // Declare request code for permissions
    private val ACTIVITY_RECOGNITION_PERMISSION_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize TextViews from the layout
        stepCountText = binding.stepCountTextView
        accelerometerText = binding.accelerometerTextView
        proximityText = binding.proximityTextView // Initialize proximity TextView

        // Get the SensorManager instance
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Get list of all Sensors and log it
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.d("DashboardFragment", "Device Sensors: ${deviceSensors.joinToString { it.name }}")

        // Get sensor instances
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) // Get proximity sensor

        // Check if Sensors available
        if (stepCounterSensor == null) {
            Toast.makeText(context, "Step Counter not available", Toast.LENGTH_SHORT).show()
        }
        if (accelerometerSensor == null) {
            Toast.makeText(context, "Accelerometer not available", Toast.LENGTH_SHORT).show()
        }
        if (proximitySensor == null) {
            Toast.makeText(context, "Proximity Sensor not available", Toast.LENGTH_SHORT).show()
        }

        // Check permission for the SENSORS
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_PERMISSION_CODE
            )
        } else {
            // Register listeners
            registerSensorListeners()
        }
    }

    // Register sensor listeners
    private fun registerSensorListeners() {
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        accelerometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) // Register proximity sensor listener
        }
    }

    // Callback for the result from requesting permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerSensorListeners()
            } else {
                Toast.makeText(context, "Permission denied for activity recognition", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                val steps = event.values[0].toInt()
                stepCountText.text = "Steps: $steps"
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                accelerometerText.text = "Accelerometer\nX: %.2f\nY: %.2f\nZ: %.2f".format(x, y, z)
            }
            Sensor.TYPE_PROXIMITY -> {
                val proximity = event.values[0]
                proximityText.text = "Proximity: %.2f cm".format(proximity) // Update proximity TextView
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not implemented
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sensorManager.unregisterListener(this)
        _binding = null
    }
}
