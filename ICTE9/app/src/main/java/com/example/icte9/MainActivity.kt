package com.example.icte9

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchPreference: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Find views by ID
        switchPreference = findViewById(R.id.switch_network_pref)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list
        adapter = NewsAdapter(emptyList())
        recyclerView.adapter = adapter

        // Load user preference and update switch state
        switchPreference.isChecked = sharedPreferences.getBoolean("allow_cellular", false)

        // Set listener to save user preference when switch changes
        switchPreference.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("allow_cellular", isChecked).apply()
        }

        findViewById<Button>(R.id.btn_refresh).setOnClickListener {
            when {
                !hasInternetConnection() -> {
                    Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
                }
                !isNetworkAllowed() -> {
                    Toast.makeText(this, "Data fetch blocked by user settings", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    fetchNews()
                }
            }
        }
    }

    private fun isNetworkAllowed(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        val allowCellular = sharedPreferences.getBoolean("allow_cellular", false)

        // Check if connection type is allowed based on user preference
        return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            allowCellular
        } else {
            false
        }
    }

    private fun hasInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = "GET YOUR OWN API KEY"
                val url = URL("https://gnews.io/api/v4/top-headlines?lang=en&topic=technology&token=$apiKey")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    val headlines = parseHeadlines(response)

                    withContext(Dispatchers.Main) {
                        adapter.updateData(headlines)
                    }
                } else {
                    showError("API returned code: $responseCode")
                }

                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error fetching news: ${e.localizedMessage}")
            }
        }
    }

    private fun parseHeadlines(jsonString: String): List<String> {
        val jsonObject = JSONObject(jsonString)
        val articles = jsonObject.getJSONArray("articles")
        val headlines = mutableListOf<String>()

        for (i in 0 until articles.length()) {
            val article = articles.getJSONObject(i)
            val title = article.getString("title")
            headlines.add(title)
        }

        return headlines
    }

    private fun showError(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

}