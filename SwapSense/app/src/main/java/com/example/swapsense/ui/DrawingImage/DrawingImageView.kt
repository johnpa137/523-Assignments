package com.example.swapsense.ui.DrawingImage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.min

@SuppressLint("ClickableViewAccessibility")
class DrawingImageView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private val path = Path()
    private val paint = Paint().apply {
        // Initialize paint with red color, 10f strokeWidth, STROKE style, anti-alias enabled
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    // Initialize with appropriate values
    private var drawCanvas: Canvas? = null
    private var bitmap: Bitmap? = null
    private var bitmapScale = 1f
    private var offsetX = 0f
    private var offsetY = 0f

    init {
        setOnTouchListener { _, event ->
            // Convert screen touch to bitmap coordinates
            val x = (event.x - offsetX) / bitmapScale
            val y = (event.y - offsetY) / bitmapScale

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Start new path at (x, y)
                    path.moveTo(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    // Extend path to (x, y) and draw on canvas
                    path.lineTo(x, y)
                    drawCanvas?.drawPath(path, paint)
                }
                MotionEvent.ACTION_UP -> {
                    // Finalize drawing, draw path and reset path
                    path.reset()
                }
            }

            // Invalidate the view to trigger redraw
            invalidate()
            true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let { bmp ->
            // Compute bitmapScale and offsets to center image in view
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val bmpWidth = bmp.width.toFloat()
            val bmpHeight = bmp.height.toFloat()

            bitmapScale = min(viewWidth / bmpWidth, viewHeight / bmpHeight)
            offsetX = (viewWidth - bmpWidth * bitmapScale) / 2f
            offsetY = (viewHeight - bmpHeight * bitmapScale) / 2f

            canvas.save()
            // Translate and scale canvas based on computed values
            canvas.translate(offsetX, offsetY)
            canvas.scale(bitmapScale, bitmapScale)

            // Draw the base bitmap
            canvas.drawBitmap(bmp, 0f, 0f, null)

            // Draw the modified bitmap with paths
            // (The drawing is already applied to the bitmap by drawCanvas)

            canvas.restore()
        }
    }

    override fun setImageBitmap(bmp: Bitmap?) {
        super.setImageBitmap(bmp)
        bmp?.let {
            // Make a mutable copy of the bitmap and create a canvas for drawing
            bitmap = it.copy(Bitmap.Config.ARGB_8888, true)
            drawCanvas = Canvas(bitmap!!)
        }
        // Handle invalidating
        invalidate()
    }

    fun getBitmapWithDrawing(): Bitmap? {
        // Return bitmap with drawing
        return bitmap
    }

    fun setBrushColor(color: Int) {
        // Set paint color and refresh view
        paint.color = color
        invalidate()
    }
}
