package com.example.mycam

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    private val imageList: MutableList<ImageItem> = mutableListOf()
    private lateinit var imageAdapter: ImageAdapter

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera: Button = findViewById(R.id.btnCamera)
        val btnGallery: Button = findViewById(R.id.btnGallery)
        val imageRecyclerView: RecyclerView = findViewById(R.id.imageRecyclerView)

        // Initialize the image adapter with the imageList
        imageAdapter = ImageAdapter(imageList)
        imageRecyclerView.layoutManager = LinearLayoutManager(this)
        imageRecyclerView.adapter = imageAdapter

        btnCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this, "Error in Opening Camera", Toast.LENGTH_SHORT).show()
            }
        }


        btnGallery.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val watermarkedBitmap = addWatermarkToImage(imageBitmap)
                    val timestamp = System.currentTimeMillis()
                    val imageItem = ImageItem(watermarkedBitmap, timestamp, false)
                    imageList.add(imageItem)
                    imageAdapter.notifyDataSetChanged()
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri: Uri = data?.data ?: return
                    val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                    val watermarkedBitmap = addWatermarkToImage(imageBitmap)
                    val timestamp = System.currentTimeMillis()
                    val imageItem = ImageItem(watermarkedBitmap, timestamp, false)
                    imageList.add(imageItem)
                    imageAdapter.notifyDataSetChanged()
                }
                // Handle other request codes here...
            }
        }
    }

    private fun addWatermarkToImage(image: Bitmap): Bitmap {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val watermarkText = "Added on $timestamp"
        val textSize = 30F
        val textColor = Color.WHITE
        val x = 20F
        val y = image.height - 20F

        val resultBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        canvas.drawText(watermarkText, x, y, paint)

        return resultBitmap
    }

}
