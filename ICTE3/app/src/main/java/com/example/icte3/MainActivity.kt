package com.example.icte3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech.EngineInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val REQUEST_CAMERA_PERMISSION = 100
const val REQUEST_STORAGE_PERMISSION = 101

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var photoUri : Uri
    private lateinit var currentPhotoPath : String

    private lateinit var takePictureLauncher : ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val openCameraButton = findViewById<Button>(R.id.openCameraButton)
        val saveImageButton = findViewById<Button>(R.id.saveImageButton)
        val resetImageButton = findViewById<Button>(R.id.resetImageButton)

        imageView = findViewById<ImageView>(R.id.imageView)

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
            if (success) {
                imageView.setImageURI(photoUri)
            }
            else {
                Toast.makeText(this, "Camera Capture Failed", Toast.LENGTH_SHORT).show()
            }
        }

        openCameraButton.setOnClickListener {
            if (checkPermissions()) {
                openCamera()
            }
            else {
                requestPermissions()
            }
        }

        saveImageButton.setOnClickListener {
            if (checkMediaPermissions()) {
                saveImage()
            }
            else {
                requestMediaPermissions()
            }
        }

        resetImageButton.setOnClickListener {
            resetImage()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION)
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            photoFile)
        takePictureLauncher.launch(photoUri)
    }

    private fun createImageFile() : File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir("Pictures")
        val image = File.createTempFile(imageFileName, "jpg", storageDir)
        currentPhotoPath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode : Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
        else if (requestCode == REQUEST_STORAGE_PERMISSION &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImage()
        }
    }

    private fun saveImage() {
        if (imageView.drawable == null) {
            Toast.makeText(this, "Capture an Image First", Toast.LENGTH_SHORT).show()
            return
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_${timeStamp}"
        val picturesDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).absolutePath

        val out = FileOutputStream("${picturesDir}/${imageFileName}.jpg")

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        out.close();

        Toast.makeText(this, "JPEG_${timeStamp} saved", Toast.LENGTH_SHORT).show()
    }

    private fun resetImage() {
        imageView.setImageResource(0)
    }

    private fun requestMediaPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_STORAGE_PERMISSION)
    }

    private fun checkMediaPermissions() : Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }
}