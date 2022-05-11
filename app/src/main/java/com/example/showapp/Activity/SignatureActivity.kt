package com.example.showapp.Activity

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.example.showapp.R
import com.example.showapp.Utils.SignatureView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_signature.*
import kotlinx.android.synthetic.main.activity_signature.view.*
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import java.io.*
import java.util.*

class SignatureActivity : AppCompatActivity() {
    private lateinit var signatureView: SignatureView
    private var bitmap: Bitmap? = null
    private var pathhh: String? = null
    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        val frameLayoutContainer = findViewById<FrameLayout>(R.id.frameLayoutContainer)
        val imageView = findViewById<ImageView>(R.id.imageView)
        signatureView = SignatureView(this).apply {
            frameLayoutContainer.addView(this)
        }
        val buttonCreate = findViewById<Button>(R.id.buttonCreate)
        buttonCreate.setOnClickListener {
            bitmap = signatureView.drawToBitmap(Bitmap.Config.ARGB_8888)
            imageView.setImageBitmap(bitmap)
        }

        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener {
            signatureView.clear()
            imageView.setImageBitmap(null)
        }
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val buttonAddSignature = findViewById<Button>(R.id.buttonAddSignature)
        buttonAddSignature.setOnClickListener {
            saveToInternalStorage(bitmap!!)
            mSharedPref.edit().apply {
                putString("pathhh",pathhh)
            }.apply()
            Toast.makeText(this,"signature added successfully",Toast.LENGTH_SHORT).show()
//            val intent = Intent(applicationContext, UserActivity::class.java)
//            startActivity(intent)
            finish()
        }

    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("images", Context.MODE_PRIVATE)
        // Create imageDir
        //val f= File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toURI())
        val mypath = File(directory, "signature.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.resizeByWidth(400).resizeByHeight(200).compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        pathhh = directory.absolutePath
        return directory.absolutePath
    }
    // Extension function to resize bitmap using new width value by keeping aspect ratio
    fun Bitmap.resizeByWidth(width:Int):Bitmap{
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val height:Int = Math.round(width / ratio)

        return Bitmap.createScaledBitmap(
            this,
            width,
            height,
            false
        )
    }


    // Extension function to resize bitmap using new height value by keeping aspect ratio
    fun Bitmap.resizeByHeight(height:Int):Bitmap{
        val ratio:Float = this.height.toFloat() / this.width.toFloat()
        val width:Int = Math.round(height / ratio)

        return Bitmap.createScaledBitmap(
            this,
            width,
            height,
            false
        )
    }


}