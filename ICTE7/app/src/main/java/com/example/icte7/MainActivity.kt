package com.example.icte7

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val deviceList = ArrayList<String>()

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val enableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) @androidx.annotation.RequiresPermission(
            android.Manifest.permission.BLUETOOTH_SCAN
        ) {
            if (bluetoothAdapter?.isEnabled == true) {
                bluetoothAdapter?.startDiscovery()
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show()
            }
        }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothDevice.ACTION_FOUND) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    val name = it.name ?: "Unnamed"
                    val address = it.address
                    val deviceInfo = "$name\n$address"
                    if (!deviceList.contains(deviceInfo)) {
                        deviceList.add(deviceInfo)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ListView and Button using findViewById
        val listView = findViewById<ListView>(R.id.deviceListView)
        val searchButton = findViewById<Button>(R.id.searchButton)
        // Set adapter to ListView and set click listener on Button
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList);
        listView.adapter = adapter
        searchButton.setOnClickListener {
            checkPermissionsAndSearch()
        }

        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun checkPermissionsAndSearch() {
        val permissions = mutableListOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), 1)
        } else {
            startBluetoothDiscovery()
        }
    }

    private fun startBluetoothDiscovery() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        } else {
            bluetoothAdapter!!.startDiscovery()
            Toast.makeText(this, "Searching for devices...", Toast.LENGTH_SHORT).show()
        }
    }
}