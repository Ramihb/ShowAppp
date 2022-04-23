package com.example.showapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.User
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserForgetPAssword2 : AppCompatActivity() {

    var loadingDialog = LoadingDialog()
    lateinit var num1: EditText
    lateinit var num2: EditText
    lateinit var num3: EditText
    lateinit var num4: EditText
    lateinit var password : EditText
    lateinit var Confirmpasswprd : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_forget_password2)
        password = findViewById(R.id.NewPassword)
        Confirmpasswprd = findViewById(R.id.ConfirmNewPassword)

        val Continue = findViewById<Button>(R.id.Continue)
        if(password.text.toString() == Confirmpasswprd.text.toString()) {
            Continue.setOnClickListener {
                //loadingDialog.startLoadingDialog()
                userChangePassword()
            }
        }else {
            Toast.makeText(applicationContext, "Check your password", Toast.LENGTH_LONG).show()
        }
    }
    private fun userChangePassword() {
        num1 = findViewById(R.id.num1)
        num2 = findViewById(R.id.num2)
        num3 = findViewById(R.id.num3)
        num4 = findViewById(R.id.num4)

        val forget = User()
        forget.email = intent.getStringExtra("email")
        forget.code = (num1.text.toString() + num2.text.toString() + num3.text.toString() + num4.text.toString()).toInt()
        forget.password = password.text.toString()
        val apiuser = ApiUser.create().resetPassword(forget)
        apiuser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    Log.i("messageeee:", response.body().toString())
                    //loadingDialog.dismissDialog()
                    navigateToMain()
                } else {
                    Toast.makeText(applicationContext, "uncorrect email", Toast.LENGTH_LONG).show()
                    Log.i("RETROFIT_API_RESPONSE", response.toString())
                    Log.i("login response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
                println("aaaaaa")
            }

        })
    }

    private fun navigateToMain() {
        finish()
        val intent = Intent(this@UserForgetPAssword2, MainActivity::class.java)
        startActivity(intent)
    }
}