package com.example.showapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.Article
import com.example.showapp.R
import com.example.showapp.Utils.LoadingDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class CompanyAddArticle : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences

    lateinit var image: ImageView
    private var selectedImageUri: Uri? = null
    private lateinit var file: File
    var loadingDialog = LoadingDialog()
    lateinit var name: EditText
    lateinit var price: EditText
    lateinit var quantity: EditText
    lateinit var details: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_add_article)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)
        loadingDialog.LoadingDialog(this)
        //Begin category Popup menu
        val categorybtn = findViewById<TextView>(R.id.ArticleCategory)
        categorybtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, categorybtn)

            popupMenu.menuInflater.inflate(R.menu.category_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.mode ->
                        categorybtn.text = "mode"
                    R.id.highTech ->
                        categorybtn.text = "high tech"
                    R.id.beauty ->
                        categorybtn.text = "beauty"
                    R.id.baby ->
                        categorybtn.text = "baby"
                    R.id.Jewellery ->
                        categorybtn.text = "Jewellery"
                    R.id.artDeco ->
                        categorybtn.text = "art deco"
                }
                true
            })
            popupMenu.show()
        }
        //End Category Ppoup menu

        //Begin type Popup menu
        val articlebtn = findViewById<TextView>(R.id.ArticleType)
        articlebtn.setOnClickListener {
            if (categorybtn.text == "mode") {
                val popupTypeMode: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeMode.menuInflater.inflate(R.menu.mode_type_menu, popupTypeMode.menu)
                popupTypeMode.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.sweaters ->
                            articlebtn.text = "sweaters"
                        R.id.pants ->
                            articlebtn.text = "pants"
                        R.id.outlet ->
                            articlebtn.text = "outlet"
                        R.id.jacket ->
                            articlebtn.text = "jacket"
                        R.id.shoes ->
                            articlebtn.text = "shoes"
                        R.id.Dress ->
                            articlebtn.text = "Dress"
                        R.id.Other ->
                            articlebtn.text = "Other"

                    }
                    true
                })
                popupTypeMode.show()
            }
            if (categorybtn.text == "high tech") {
                val popupTypeHighTech: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeHighTech.menuInflater.inflate(
                    R.menu.high_tech_menu,
                    popupTypeHighTech.menu
                )
                popupTypeHighTech.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.Pc ->
                            articlebtn.text = "Pc"
                        R.id.Tv ->
                            articlebtn.text = "Tv"
                        R.id.ComputersAccessories ->
                            articlebtn.text = "Computers' accessories"
                        R.id.Other ->
                            articlebtn.text = "Other"

                    }
                    true
                })
                popupTypeHighTech.show()
            }

            if (categorybtn.text == "beauty") {
                val popupTypeBeauty: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeBeauty.menuInflater.inflate(R.menu.beauty_menu, popupTypeBeauty.menu)
                popupTypeBeauty.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.makeUp ->
                            articlebtn.text = "make-up "
                        R.id.Other ->
                            articlebtn.text = "Other"
                    }
                    true
                })
                popupTypeBeauty.show()
            }
            if (categorybtn.text == "baby") {
                val popupTypeBaby: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeBaby.menuInflater.inflate(R.menu.baby_menu, popupTypeBaby.menu)
                popupTypeBaby.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.Baby ->
                            articlebtn.text = "Baby"
                        R.id.Other ->
                            articlebtn.text = "Other"
                    }
                    true
                })
                popupTypeBaby.show()
            }
            if (categorybtn.text == "Jewellery") {
                val popupTypeJewellery: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeJewellery.menuInflater.inflate(
                    R.menu.jewellery_menu,
                    popupTypeJewellery.menu
                )
                popupTypeJewellery.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.earrings ->
                            articlebtn.text = "earrings"
                        R.id.rings ->
                            articlebtn.text = "rings"
                        R.id.bracelets ->
                            articlebtn.text = "bracelets"
                        R.id.Other ->
                            articlebtn.text = "Other"
                    }
                    true
                })
                popupTypeJewellery.show()
            }
            if (categorybtn.text == "art deco") {
                val popupTypeArtDeco: PopupMenu = PopupMenu(this, articlebtn)
                popupTypeArtDeco.menuInflater.inflate(R.menu.artdeco_menu, popupTypeArtDeco.menu)
                popupTypeArtDeco.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.artDeco ->
                            articlebtn.text = "art deco"
                        R.id.ArtSupplies ->
                            articlebtn.text = "Art Supplies"
                        R.id.Other ->
                            articlebtn.text = "Other"
                    }
                    true
                })
                popupTypeArtDeco.show()
            }
        }
        //End Popup menu

        image = findViewById<ImageView>(R.id.ArticleImageAdd)
        val addImage = findViewById<Button>(R.id.AddImageArticle)

        addImage.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(it, 123)
            }
        }

        name = findViewById(R.id.ArticleName)
        details = findViewById(R.id.ArticleDescription)
        price = findViewById(R.id.ArticlePrice)
        quantity = findViewById(R.id.ArticleQuantity)
        val y = mSharedPref.getString("brandID", null)
        val btn = findViewById<Button>(R.id.AddArticleBtn)
        btn.setOnClickListener {
            if (name.text.toString().isEmpty()) {
                name.error = "name required"
                name.requestFocus()
                return@setOnClickListener
            }
            if (price.text.toString().isEmpty()) {
                price.error = "price required"
                price.requestFocus()
                return@setOnClickListener
            }
            if (quantity.text.toString().isEmpty()) {
                quantity.error = "quantity required"
                quantity.requestFocus()
                return@setOnClickListener
            }
            if (details.text.toString().isEmpty()) {
                details.error = "details required"
                details.requestFocus()
                return@setOnClickListener
            }
            loadingDialog.startLoadingDialog()
            uploadImage(
                name.text.toString(),
                categorybtn.text.toString(),
                price.text.toString(),
                quantity.text.toString().toInt(),
                articlebtn.text.toString(),
                y.toString(),
                details.text.toString()
            )
            name.text.clear()
            price.text.clear()
            quantity.text.clear()
            details.text.clear()
            image.setImageResource(R.drawable.ic_baseline_image_24)
            name.requestFocus()
            categorybtn.setText(getString(R.string.chose_your_category))
            articlebtn.setText(getString(R.string.chose_your_article_type))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)

            println("jcpsqd" + selectedImageUri)

        }
    }

    private fun uploadImage(
        name: String,
        category: String,
        price: String,
        quantity: Int,
        type: String,
        brand: String,
        details: String
    ) {
        if (selectedImageUri == null) {
            println("image null")

            return
        }


        val stream = contentResolver.openInputStream(selectedImageUri!!)
        val request =
            stream?.let {
                RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    it.readBytes()
                )
            } // read all bytes using kotlin extension
        val articlePicture = request?.let {
            MultipartBody.Part.createFormData(
                "articlePicture",
                "image.jpeg",
                it
            )
        }


        Log.d("MyActivity", "on finish upload file")

        val apiInterface = ApiCompany.create()
        val data: LinkedHashMap<String, RequestBody> = LinkedHashMap()

        data["name"] = RequestBody.create(MultipartBody.FORM, name)
        data["category"] = RequestBody.create(MultipartBody.FORM, category)
        data["price"] = RequestBody.create(MultipartBody.FORM, price)
        data["quantity"] = RequestBody.create(MultipartBody.FORM, quantity.toString())
        data["type"] = RequestBody.create(MultipartBody.FORM, type)
        data["brand"] = RequestBody.create(MultipartBody.FORM, brand)
        data["Details"] = RequestBody.create(MultipartBody.FORM, details)

        if (articlePicture != null) {
            apiInterface.addArticle(data, articlePicture)
                .enqueue(object : Callback<Article> {
                    override fun onResponse(
                        call: Call<Article>,
                        response: Response<Article>
                    ) {
                        if (response.isSuccessful) {
                            loadingDialog.dismissDialog()

                            Log.i("onResponse goooood", response.body().toString())
                            Toast.makeText(applicationContext, "article added", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Article>, t: Throwable) {
                        println("noooooooooooooooooo")
                    }

                })
        }
    }

    override fun onBackPressed() {
        finish()
        val intent = Intent(this@CompanyAddArticle, CompanyProfile::class.java)
        startActivity(intent)
    }

}
