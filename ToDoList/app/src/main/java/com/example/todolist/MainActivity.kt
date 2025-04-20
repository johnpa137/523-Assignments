package com.example.todolist

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var taskList : MutableList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // connect RecyclerView in XML
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // connect add task button in XML
        val addTaskButton = findViewById<Button>(R.id.addTasksButton)

        // set layout manager to control how items are displayed (vertical list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // provide initial list of task placeholders
        taskList = mutableListOf("Task 1")

        // attach adapter to bind data to the RecyclerView
        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        addTaskButton.setOnClickListener {
            val pos = taskList.size
            taskList.add(pos, "Task ${pos + 1}")
            taskAdapter.notifyItemInserted(pos)
        }
    }
}