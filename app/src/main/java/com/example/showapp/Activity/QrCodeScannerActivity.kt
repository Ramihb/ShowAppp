package com.example.showapp.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.showapp.R

class QrCodeScannerActivity : AppCompatActivity() {

    private lateinit var codescanner: CodeScanner
    lateinit var articleName:String
    lateinit var articlePrice:String
    lateinit var articlePicture:String
    lateinit var id:String
    lateinit var details:String
    lateinit var type:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }else {
            startScanning()
        }
    }
    private fun startScanning(){
        val scannerview: CodeScannerView = findViewById(R.id.scanner_view)
        codescanner = CodeScanner(this, scannerview)
        codescanner.camera = CodeScanner.CAMERA_BACK
        codescanner.formats = CodeScanner.ALL_FORMATS

        codescanner.autoFocusMode = AutoFocusMode.SAFE
        codescanner.scanMode = ScanMode.SINGLE
        codescanner.isAutoFocusEnabled = true

        codescanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"Scan Result: ${it.text}", Toast.LENGTH_SHORT).show()
                Log.i("scan Result: ", it.text)
                articleName = it.text.substringBefore(",").trim()
                var textt = it.text.substringAfter(",")
                articlePrice = textt.substringBefore(",,").trim()
                textt = textt.substringAfter(",,")
                articlePicture = textt.substringBefore(",,,").trim()
                textt = it.text.substringAfter(",,,")
                id = textt.substringBefore(",,,,").trim()
                textt = it.text.substringAfter(",,,,")
                details = textt.substringBefore(",,,,,").trim()
                //textt = it.text.substringAfter(",,,,,")
                type = it.text.substringAfter(",,,,,").trim()
                Log.i("article nam: ", articleName.toString())
                Log.i("article price: ", articlePrice.toString())
                Log.i("article picture: ", articlePicture.toString())
                Log.i("article id: ", id.toString())
                Log.i("article details: ", details.toString())
                Log.i("article type: ", type.toString())
                navigateToUserArticleDetails()
            }
        }

        codescanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Camera initialization error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        scannerview.setOnClickListener {
            codescanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 123) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                startScanning()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(::codescanner.isInitialized){
            codescanner.startPreview()
        }
    }

    override fun onPause() {
        if(::codescanner.isInitialized){
            codescanner.releaseResources()
        }
        super.onPause()
    }
    private fun navigateToUserArticleDetails() {
        finish()
        val intent = Intent(this@QrCodeScannerActivity, UserArticleDetails::class.java)
        intent.putExtra("articleName",articleName)
        intent.putExtra("articlePrice",articlePrice)
        intent.putExtra("articlePicture",articlePicture)
        intent.putExtra("id",id)
        intent.putExtra("details",details)
        intent.putExtra("type",type)
        startActivity(intent)
    }
}