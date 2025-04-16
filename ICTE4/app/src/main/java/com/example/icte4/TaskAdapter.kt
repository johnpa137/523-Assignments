package com.example.icte4

import android.app.TimePickerDialog
import android.graphics.Paint
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<String>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    // View holder defines how each item is managed in the RecylcerView, inner Class
    inner class TaskViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val taskEditText : EditText = itemView.findViewById(R.id.taskEditText)
        val pickTimeButton : Button = itemView.findViewById(R.id.pickTimeButton)
        val timeText : TextView = itemView.findViewById(R.id.timeText)

        init {
            // Toggle the strikethrough with clicking
            taskEditText.setOnClickListener {
                val isStruckThrough = taskEditText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG != 0
                taskEditText.paintFlags = if(isStruckThrough) {
                    taskEditText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // removes it
                }
                else {
                    taskEditText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // removes it
                }
            }

            // show time picker when button is clicked
            pickTimeButton.setOnClickListener {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                // launch time picker dialog
                val timePicker = TimePickerDialog(itemView.context, {
                    _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    timeText.text = "Selected Time : $formattedTime"
                }, hour, minute, true)
                timePicker.show()
            }
        }
    }

    // inflate the layout for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    // bind data to each View Holder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.taskEditText.setText(tasks[position])
    }
}