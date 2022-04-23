package com.example.showapp.Utils


import android.app.Activity
import android.app.AlertDialog
import com.example.showapp.R


class LoadingDialog {

    var activity: Activity? = null
    lateinit var dialog: AlertDialog

    fun LoadingDialog(myActivity: Activity?) {
        activity = myActivity
    }

    fun startLoadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        //dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}