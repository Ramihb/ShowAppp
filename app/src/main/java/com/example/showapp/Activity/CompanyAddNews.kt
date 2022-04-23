package com.example.showapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.New
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyAddNews : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var image:ImageView
    private var selectedImageUri: Uri? = null
    var loadingDialog = LoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_add_news)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)
        loadingDialog.LoadingDialog(this)

        image = findViewById(R.id.newsImage)

        image.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 123)
            }
        }
        val Title = findViewById<EditText>(R.id.newsTitle)
        val addNewsBtn = findViewById<Button>(R.id.addNews)


        addNewsBtn.setOnClickListener {
            loadingDialog.startLoadingDialog()
            addNews(Title.text.toString(),mSharedPref.getString("brandID", null).toString())
            Title.text.clear()
            image.setImageResource(R.drawable.ic_baseline_image_24)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 123) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)
        }
    }

    //begin add news
    private fun addNews(title: String, brandId: String){
        if(selectedImageUri == null){
            println("image null")

            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it.readBytes()) } // read all bytes using kotlin extension
        val newsPicture = request?.let {
            MultipartBody.Part.createFormData(
                "newsPicture",
                "image.jpeg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")

        val apiInterface = ApiCompany.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["BrandsName"] = RequestBody.create(MultipartBody.FORM, brandId)
        data["title"] = RequestBody.create(MultipartBody.FORM, title)

        if (newsPicture != null) {
            apiInterface.addNews(data,newsPicture).enqueue(object:
                Callback<New> {
                override fun onResponse(
                    call: Call<New>,
                    response: Response<New>
                ) {
                    if (response.isSuccessful) {
                        Log.i("onResponse goooood", response.body().toString())
                        loadingDialog.dismissDialog()
                        Toast.makeText(applicationContext, "news added", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "news not added", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                override fun onFailure(call: Call<New>, t: Throwable) {
                    println("noooooooooooooooooo")
                }

            })
        }
    }
    //end add news
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@CompanyAddNews, CompanyNewsList::class.java)
        startActivity(intent)
    }
}