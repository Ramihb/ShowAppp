package com.example.showapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.User
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserForgetPassword : AppCompatActivity() {
    lateinit var loginMail: EditText
    var loadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_forget_password)
        loadingDialog.LoadingDialog(this)
        val ContinueBtn = findViewById<Button>(R.id.Continue)
        ContinueBtn.setOnClickListener {
            loadingDialog.startLoadingDialog()
            userMail()
        }

    }


    private fun userMail() {
        loginMail = findViewById(R.id.EmailInput)
        val userr = User()
        userr.email = loginMail.text.toString()
        val apiuser = ApiUser.create().forgetPassword(userr)
        apiuser.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    Log.i("login User:", response.body().toString())
                    loadingDialog.dismissDialog()
                    navigateToF2()
                } else {
                    Toast.makeText(applicationContext, "uncorrect email", Toast.LENGTH_LONG).show()
                    Log.i("RETROFIT_API_RESPONSE", response.toString())
                    Log.i("login response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                println("aaaaaa")
            }

        })
    }

    private fun navigateToF2() {
        finish()
        val intent = Intent(this@UserForgetPassword, UserForgetPAssword2::class.java)
        intent.putExtra("email",loginMail.text.toString())
        startActivity(intent)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@UserForgetPassword, MainActivity::class.java)
        startActivity(intent)
    }
}