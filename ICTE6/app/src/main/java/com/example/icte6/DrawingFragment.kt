package com.example.icte6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class DrawingFragment : Fragment() {

    private lateinit var drawingView: DrawingView
    private lateinit var clearButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drawing, container, false)

        drawingView = view.findViewById(R.id.drawingView)
        clearButton = view.findViewById(R.id.clearButton)

        clearButton.setOnClickListener {
            drawingView.clearCanvas()
        }

        return view
    }
}