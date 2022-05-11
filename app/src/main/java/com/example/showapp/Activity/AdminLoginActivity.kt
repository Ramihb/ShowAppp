package com.example.showapp.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Admin
import com.example.showapp.Model.Company
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminLoginActivity : AppCompatActivity() {
    lateinit var loginMail: EditText
    lateinit var loginPassword: EditText
    var loadingDialog = LoadingDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        val loginBtn = findViewById<Button>(R.id.LoginBtn)
        loadingDialog.LoadingDialog(this)
        loginBtn.setOnClickListener {
            loginMail = findViewById(R.id.EmailInput)
            loginPassword = findViewById(R.id.PasswordInput)
            if (loginMail.text.toString().isEmpty()) {
                loginMail.error = "Email required"
                loginMail.requestFocus()
                return@setOnClickListener
            }
            if (loginPassword.text.toString().isEmpty()) {
                loginPassword.error = "password required"
                loginPassword.requestFocus()
                return@setOnClickListener
            }
            loadingDialog.startLoadingDialog()

            adminLogin()

        }
    }

    //Begin Admin Login
    private fun adminLogin() {
        loginMail = findViewById(R.id.EmailInput)
        loginPassword = findViewById(R.id.PasswordInput)
        val adminn = Admin()
        adminn.email = loginMail.text.toString()
        adminn.password = loginPassword.text.toString()
        val apicompany = ApiUser.create().loginAdmin(adminn)
        apicompany.enqueue(object : Callback<Admin> {
            override fun onResponse(call: Call<Admin>, response: Response<Admin>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_LONG).show()
                    Log.i("login Admin", response.body().toString())

                    loadingDialog.dismissDialog()

                    //navigateToCompanyProfile()

                } else {
                    //Toast.makeText(applicationContext, "uncorrect email or password", Toast.LENGTH_LONG).show()
                    Log.i("RETROFIT_API_RESPONSE", response.toString())
                    Log.i("login response", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Admin>, t: Throwable) {
                Toast.makeText(applicationContext, "erreur server", Toast.LENGTH_LONG).show()
            }

        })
    }

    //End Admin Login
}
