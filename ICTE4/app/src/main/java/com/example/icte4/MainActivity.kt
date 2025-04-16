package com.example.icte4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // connect RecyclerView in XML
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // set layout manager to control how items are displayed (vertical list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // provide initial list of task placeholders
        val taskList = listOf("Buy Groceries", "Do your Homework", "Walk the dog")

        // attach adapter to bind data to the RecyclerView

        recyclerView.adapter = TaskAdapter(taskList)

    }
}