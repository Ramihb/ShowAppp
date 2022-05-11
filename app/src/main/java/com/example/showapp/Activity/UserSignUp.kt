package com.example.showapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.User
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSignUp : AppCompatActivity() {

    lateinit var image:ImageView
    private var selectedImageUri: Uri? = null
    private var newsLettre: Boolean? = null
    var loadingDialog = LoadingDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)
        val addImage = findViewById<Button>(R.id.AddImage)
        image = findViewById<ImageView>(R.id.UserImage)
        loadingDialog.LoadingDialog(this)
        addImage.setOnClickListener{
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 123)
            }
        }
        val firstName = findViewById<EditText>(R.id.FirstNameSP)
        val lastName = findViewById<EditText>(R.id.LastNameSP)
        val email = findViewById<EditText>(R.id.EmailSP)
        val password = findViewById<EditText>(R.id.Password)
        val confirmPassword = findViewById<EditText>(R.id.ConfirmPassword)
        val number = findViewById<EditText>(R.id.PhoneNumberSP)

        val loginBtn = findViewById<Button>(R.id.Continue)
        loginBtn.setOnClickListener {
            if(firstName.text.toString().isEmpty()){
                firstName.error = "firstName required"
                firstName.requestFocus()
                return@setOnClickListener
            }
            if(lastName.text.toString().isEmpty()){
                lastName.error = "lastName required"
                lastName.requestFocus()
                return@setOnClickListener
            }
            if(email.text.toString().isEmpty()){
                email.error = "email required"
                email.requestFocus()
                return@setOnClickListener
            }
            if(password.text.toString().isEmpty()){
                password.error = "password required"
                password.requestFocus()
                return@setOnClickListener
            }
            if(confirmPassword.text.toString().isEmpty()){
                confirmPassword.error = "confirm password required"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }
            if(number.text.toString().isEmpty()){
                number.error = "phone number required"
                number.requestFocus()
                return@setOnClickListener
            }
            if(password.text.toString() == confirmPassword.text.toString()) {
                loadingDialog.startLoadingDialog()
                login(
                    firstName.text.toString(),
                    lastName.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    number.text.toString()
                )
            } else {
                confirmPassword.error = "password dosen't match"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)
        }
    }

    //begin login user
    private fun login(firstName: String, lastName: String, email: String, password: String, number: String){
        if(selectedImageUri == null){
            println("image null")

            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
        val profilePicture = request?.let {
            MultipartBody.Part.createFormData(
                "profilePicture",
                "image.jpeg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")

        val apiInterface = ApiUser.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()
        newsLettre = true
        data["firstName"] = RequestBody.create(MultipartBody.FORM, firstName)
        data["lastName"] = RequestBody.create(MultipartBody.FORM, lastName)
        data["email"] = RequestBody.create(MultipartBody.FORM, email)
        data["password"] = RequestBody.create(MultipartBody.FORM, password)
        data["phoneNumber"] = RequestBody.create(MultipartBody.FORM, number)
        data["newsLettre"] = RequestBody.create(MultipartBody.FORM, newsLettre.toString())

        if (profilePicture != null) {
            apiInterface.userSignUp(data,profilePicture).enqueue(object:
                Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    Log.i("waaaa", response.body().toString())
                    if(response.isSuccessful){
                        Log.i("onResponse goooood", response.body().toString())
                        loadingDialog.dismissDialog()
                        showAlertDialog()
                    } else {
                        Log.i("OnResponse not good", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println("noooooooooooooooooo")
                    loadingDialog.dismissDialog()
                }

            })
        }
    }
    //end login user

    //begin Alert dialog
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("Thank you for choosing showapp.tn! \n We have sent you an email to confirm your account")
            .setPositiveButton("Ok") {dialog, which ->
                navigateToLogin()
            }
            .show()
    }
    //end Alert dialog

    //snackbar
    private fun showSnackbar(msg: String) {
        Snackbar.make(signUpView, msg, Snackbar.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@UserSignUp, MainActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToLogin() {
        finish()
        val intent = Intent(this@UserSignUp, MainActivity::class.java)
        startActivity(intent)
    }
}