package com.example.showapp.Activity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.showapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_finished_orders.*
import java.io.File

class FinishedOrders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished_orders)


        val filePDF = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/showapp files/Bill.pdf")
                .toURI()
        )
        if (filePDF.exists()) {
            Log.i("bonjour", filePDF.exists().toString())
            pdfView.fromFile(filePDF)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .load()
        } else {
            Log.i("bonsoir", filePDF.exists().toString())
            MaterialAlertDialogBuilder(this)
                .setTitle("Alert")
                .setMessage("no finished orders yet")
                .setPositiveButton("Ok") {dialog, which ->
                    finish()
                }
                .show()
        }

    }

}
