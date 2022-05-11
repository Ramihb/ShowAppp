package com.example.showapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.Company
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanySignUp : AppCompatActivity() {

    lateinit var image:ImageView
    private var selectedImageUri: Uri? = null
    var loadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_sign_up)
        loadingDialog.LoadingDialog(this)
        image = findViewById(R.id.UserImage)
        val addImage = findViewById<Button>(R.id.AddImage)
        //Begin category Popup menu
        var categroy = findViewById<TextView>(R.id.TypeOfBusiness)
        categroy.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, categroy)
            popupMenu.menuInflater.inflate(R.menu.category_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.mode ->
                        categroy.text = "mode"
                    R.id.highTech ->
                        categroy.text = "high tech"
                    R.id.beauty ->
                        categroy.text = "beauty"
                    R.id.baby ->
                        categroy.text = "baby"
                    R.id.Jewellery ->
                        categroy.text = "Jewellery"
                    R.id.artDeco ->
                        categroy.text = "art deco"
                }
                true
            })
            popupMenu.show()
        }
        //End Category Ppoup menu

        addImage.setOnClickListener{
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 123)
            }
        }

        val firstName = findViewById<EditText>(R.id.BOFirstName)
        val lastName = findViewById<EditText>(R.id.BOLastName)
        val brandName = findViewById<EditText>(R.id.BrandName)
        val number = findViewById<EditText>(R.id.ContactNumber)
        val email = findViewById<EditText>(R.id.BrandEmail)
        val password = findViewById<EditText>(R.id.BrandPassword)
        val confirmPassword = findViewById<EditText>(R.id.BrandConfirmPassword)
        var RegisterBusinessBtn = findViewById<Button>(R.id.RegisterBusiness)
        RegisterBusinessBtn.setOnClickListener {
            loadingDialog.startLoadingDialog()
            uploadImage(firstName.text.toString(),lastName.text.toString(),brandName.text.toString(),number.text.toString().toInt(),email.text.toString(),password.text.toString(),categroy.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImage(firstName: String, lastName: String, brandName: String, number: Int, email: String, password: String, RegisterBusinessBtn: String){
        if(selectedImageUri == null){
            println("image null")

            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
        val brandPicCompany = request?.let {
            MultipartBody.Part.createFormData(
                "brandPicCompany",
                "image.jpeg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")

        val apiInterface = ApiCompany.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["firstNameCompany"] = RequestBody.create(MultipartBody.FORM, firstName)
        data["lastNameCompany"] = RequestBody.create(MultipartBody.FORM, lastName)
        data["businessNameCompany"] = RequestBody.create(MultipartBody.FORM, brandName)
        data["phoneNumberCompany"] = RequestBody.create(MultipartBody.FORM, number.toString())
        data["emailCompany"] = RequestBody.create(MultipartBody.FORM, email)
        data["passwordCompany"] = RequestBody.create(MultipartBody.FORM, password)
        data["categoryCompany"] = RequestBody.create(MultipartBody.FORM, RegisterBusinessBtn)

        if (brandPicCompany != null) {
            apiInterface.companySignUp(data,brandPicCompany).enqueue(object:
                Callback<Company> {
                override fun onResponse(
                    call: Call<Company>,
                    response: Response<Company>
                ) {
                    Log.i("onResponse goooood", response.body().toString())
                    loadingDialog.dismissDialog()
                    showAlertDialog()
                }

                override fun onFailure(call: Call<Company>, t: Throwable) {
                    println("noooooooooooooooooo")
                    loadingDialog.dismissDialog()
                }

            })
        }
    }
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@CompanySignUp, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToTermsAndConditions() {
        val intent = Intent(this, TermsAndConditionsActivity::class.java)
        startActivity(intent)
    }
    //begin Alert dialog
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("Thank you for choosing showapp.tn! \n We have sent you an email to confirm your account \n You must accept user terms and conditions")
            .setPositiveButton("Ok") {dialog, which ->
                //showSnackbar("welcome")
                navigateToTermsAndConditions()
            }
            .show()
    }
    //end Alert dialog
}