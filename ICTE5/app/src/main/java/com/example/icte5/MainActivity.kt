package com.example.icte5

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

// MainActivity.kt (basic structure)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the DrawingView and buttons
        val drawingView = findViewById<DrawingView>(R.id.drawingView)
        val blackBrushButton = findViewById<Button>(R.id.blackBrushButton)
        val redBrushButton = findViewById<Button>(R.id.redBrushButton)
        val blueBrushButton = findViewById<Button>(R.id.blueBrushButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val detectButton = findViewById<Button>(R.id.detectButton)

        // Hook up color buttons to change brush color
        blackBrushButton.setOnClickListener {
            drawingView.setColor(Color.BLACK)
        }
        redBrushButton.setOnClickListener {
            drawingView.setColor(Color.RED)
        }
        blueBrushButton.setOnClickListener {
            drawingView.setColor(Color.BLUE)
        }

        // Hook up clear button to reset canvas
        clearButton.setOnClickListener {
            drawingView.clearCanvas()
        }

        detectButton.setOnClickListener {
            detectTextFromDrawing(drawingView.getBitmap())
        }
    }

    private fun detectTextFromDrawing(bitmap : Bitmap) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image).addOnSuccessListener {
            visionText ->
            val detectedText = visionText.text
            Toast.makeText(this, detectedText.ifEmpty { "No Text Detected" }, Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            e -> Toast.makeText(this, "Failed to Detect Text: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}