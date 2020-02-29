package com.example.list_alpha

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.FileOutputStream


class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }

    fun browseButtonClicked(view: View) {
        Glide.with(this)
            .asBitmap()
            .load("https://source.unsplash.com/random/600×500/?cat")
            .into(object : SimpleTarget<Bitmap>( 200, 200) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveImage(resource)
                }
            })
    }

    //code to save the image

    private fun saveImage(resource: Bitmap) {

        var savedImagePath: String? = null
        val Random_Number = (0..1000000).random()
        val imageFileName = "JPEG_" + Random_Number + ".jpg"

        val storageDir = File(Environment.getExternalStorageDirectory().absolutePath + "/Pics")

        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }

        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                resource.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show()
        }
    }

    // Add the image to the system gallery
    private fun galleryAddPic(imagePath: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(imagePath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
        } else {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://mnt/sdcard/" + Environment.getExternalStorageDirectory())
                )
            )
        }


    }
}
