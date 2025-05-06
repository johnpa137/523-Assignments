package com.example.swapsense.ui.DrawingImage

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.example.swapsense.R
import java.io.IOException

class DrawingImageFragment : Fragment() {

    private lateinit var drawingImageView: DrawingImageView
    private var originalBitmap: Bitmap? = null
    private var currentImageUri: Uri? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                currentImageUri = it

                try {
                    // Load bitmap from URI
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val rawBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    val exifInputStream = requireContext().contentResolver.openInputStream(uri)
                    val exif = exifInputStream?.let { stream -> ExifInterface(stream) }
                    val orientation = exif?.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    ) ?: ExifInterface.ORIENTATION_NORMAL

                    val matrix = Matrix()
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    }

                    val rotatedBitmap = Bitmap.createBitmap(
                        rawBitmap,
                        0, 0,
                        rawBitmap.width,
                        rawBitmap.height,
                        matrix,
                        true
                    )

                    originalBitmap = rotatedBitmap
                    drawingImageView.setImageBitmap(rotatedBitmap)

                } catch (e: Exception) {
                    // Handle Exception
                    Toast.makeText(context, "Error loading image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_draw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        drawingImageView = view.findViewById(R.id.drawingImageView)

        // Launch image picker
        view.findViewById<Button>(R.id.selectImageButton).setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        // Save drawn image as new image in gallery
        view.findViewById<Button>(R.id.saveImageButton).setOnClickListener {
            saveImageAsNew()
        }

        // Reset the drawing to originalBitmap
        view.findViewById<Button>(R.id.resetButton).setOnClickListener {
            originalBitmap?.let {
                drawingImageView.setImageBitmap(it)
            }
        }

        // Set brush color to black
        view.findViewById<Button>(R.id.blackColorButton).setOnClickListener {
            drawingImageView.setBrushColor(Color.BLACK)
        }

        // Set brush color to red
        view.findViewById<Button>(R.id.redColorButton).setOnClickListener {
            drawingImageView.setBrushColor(Color.RED)
        }
    }

    private fun saveImageAsNew() {
        // Get bitmap with drawing from view
        val bitmap = drawingImageView.getBitmapWithDrawing()

        // Create content values and insert into MediaStore
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "drawing_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SwapSense")
            }
        }

        val resolver = requireContext().contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            saveImageToUri(it)
        } ?: Toast.makeText(context, "Failed to create image file", Toast.LENGTH_SHORT).show()
    }

    private fun saveImageToUri(uri: Uri) {
        try {
            val outputStream = requireContext().contentResolver.openOutputStream(uri)
            val bitmap = drawingImageView.getBitmapWithDrawing()
            val saved = outputStream?.let { bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it) }
            outputStream?.close()

            if (saved == true) {
                Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
