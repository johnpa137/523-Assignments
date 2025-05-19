package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*
import coil.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.ktor.http.HttpStatusCode


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var client: HttpClient

    private val apiKey = "USE YOUR OWN API KEY"

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getLastLocationAndFetchWeather()
        } else {
            showToast("Location permission denied. Please enter a city manually.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location on start
        if (isConnected()) {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // View Binding setup
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Ktor client
        client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // Button listener
        binding.btnSearch.setOnClickListener {
            val city = binding.editTextLocation.text.toString().trim()
            when {
                city.isEmpty() -> {
                    showToast("City name cannot be blank")
                }
                !isConnected() -> {
                    showToast("Please connect to internet")
                }
                else -> {
                    fetchWeather(city)
                }
            }
        }
    }

    private fun getLastLocationAndFetchWeather() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                fetchWeatherByCoordinates(it.latitude, it.longitude)
            } ?: showToast("Couldn't get your location. Try entering a city.")
        }.addOnFailureListener {
            showToast("Location unavailable. Try entering a city.")
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchWeather(city: String) {
        lifecycleScope.launch {
            try {
                val response = client.get("https://api.openweathermap.org/data/2.5/weather") {
                    parameter("q", city)
                    parameter("appid", apiKey)
                    parameter("units", "imperial")
                }

                if (response.status == HttpStatusCode.OK) {
                    val weatherData: WeatherResponse = response.body()
                    updateUI(weatherData)
                } else if (response.status == HttpStatusCode.NotFound) {
                    showToast("City not found. Please enter a valid city name.")
                } else {
                    showToast("Unexpected error: ${response.status.description}")
                }

            } catch (e: Exception) {
                showToast("Failed to connect or parse data: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    private fun fetchWeatherByCoordinates(lat: Double, lon: Double) {
        lifecycleScope.launch {
            try {
                val response = client.get("https://api.openweathermap.org/data/2.5/weather") {
                    parameter("lat", lat)
                    parameter("lon", lon)
                    parameter("appid", apiKey)
                    parameter("units", "imperial")
                }

                if (response.status == HttpStatusCode.OK) {
                    val weatherData: WeatherResponse = response.body()
                    updateUI(weatherData)
                } else if (response.status == HttpStatusCode.NotFound) {
                    showToast("City not found. Please enter a valid city name.")
                } else {
                    showToast("Unexpected error: ${response.status.description}")
                }

            } catch (e: Exception) {
                showToast("Failed to connect or parse data: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }


    private fun updateUI(data: WeatherResponse) {
        val iconCode = data.weather.firstOrNull()?.icon ?: "01d"
        val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
        binding.imgWeatherIcon.load(iconUrl)
        binding.txtCityName.text = data.name
        binding.txtCurrentTemp.text = "${data.main.temp.toInt()}°F"
        binding.txtCondition.text = data.weather.firstOrNull()?.main ?: "N/A"
        binding.txtMinTemp.text = "Min: ${data.main.tempMin.toInt()}°F"
        binding.txtMaxTemp.text = "Max: ${data.main.tempMax.toInt()}°F"
        binding.txtWindSpeed.text = "💨 Wind: ${data.wind.speed} mph"
        binding.txtPressure.text = "🌡️ Pressure: ${data.main.pressure} hPa"
        binding.txtHumidity.text = "💧 Humidity: ${data.main.humidity}%"
        binding.txtSunrise.text = "Sunrise: ${formatUnixTime(data.sys.sunrise)}"
        binding.txtSunset.text = "Sunset: ${formatUnixTime(data.sys.sunset)}"
    }

    private fun formatUnixTime(unixTime: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(unixTime * 1000))
    }

    override fun onDestroy() {
        super.onDestroy()
        client.close()
    }
}
