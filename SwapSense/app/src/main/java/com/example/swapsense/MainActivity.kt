package com.example.swapsense

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapsense.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the BottomNavigationView
        val navView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        // Initialize NavController from the fragment host

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Create AppBarConfiguration with the fragments' ids
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_dashboard, R.id.navigation_camera, R.id.navigation_draw)
        )

        // Set up the toolbar with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up BottomNavigationView with the NavController
        navView.setupWithNavController(navController)
    }

    // Optionally, handle back navigation in the toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
