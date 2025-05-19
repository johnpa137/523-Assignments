package com.example.icte6

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.createBitmap

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    // Create Paint object to define brush style
    private var paint : Paint = Paint()
    // Create Path object to store finger movement

    private var path : Path = Path()
    // Create Bitmap and Canvas to hold your drawing
    private lateinit var bitmap : Bitmap
    private lateinit var drawCanvas : Canvas

    // Setup paint properties: stroke width, style, color
    init {
        paint.strokeWidth = 50.0f
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Initialize Bitmap and Canvas with correct size
        bitmap = createBitmap(w, h)
        drawCanvas = Canvas(bitmap)
        clearCanvas()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw existing Bitmap first
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint)
        // Then draw current Path over the Bitmap
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            // Update Path with coordinates from event (moveTo, lineTo, etc.)
            MotionEvent.ACTION_DOWN -> path.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> path.lineTo(touchX, touchY)
            // When finger is lifted, draw the path on canvas and reset it
            MotionEvent.ACTION_UP -> {
                drawCanvas.drawPath(path, paint)
                path.reset()
            }
        }

        // Call invalidate() to trigger redraw
        invalidate()
        return true
    }

    fun getBitmap() : Bitmap {
        return bitmap
    }

    fun setColor(color: Int) {
        // Change Paint color for future drawings
        paint.color = color
    }

    fun clearCanvas() {
        // Fill canvas with white and invalidate
        drawCanvas.drawColor(Color.WHITE)
        invalidate()
    }
}