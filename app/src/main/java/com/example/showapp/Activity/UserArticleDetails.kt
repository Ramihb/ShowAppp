package com.example.showapp.Activity

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Adapter.ProductAdapter
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.*
import com.example.showapp.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_article_details.*
import kotlinx.android.synthetic.main.activity_user_article_details.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserArticleDetails : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    var num = 1
    var isChecked = false
    lateinit var RecomRecView_ProductDetailsPage: RecyclerView
    var type :String?= null
    //lateinit var data : MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val name = intent.getStringExtra("articleName")
        val price = intent.getStringExtra("articlePrice")
        val picture = intent.getStringExtra("articlePicture")
        val id = intent.getStringExtra("id")
        val details = intent.getStringExtra("details")
        type = intent.getStringExtra("type")

        val namee = findViewById<TextView>(R.id.NameArticleDetails)
        val pricee = findViewById<TextView>(R.id.PriceArticleDetails)
        val picturee = findViewById<ImageView>(R.id.ImageArticleDetails)
        val detailss = findViewById<TextView>(R.id.DetailArticleDetails)
         RecomRecView_ProductDetailsPage = findViewById(R.id.RecomRecView_ProductDetailsPage)
        RecomRecView_ProductDetailsPage.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        RecomRecView_ProductDetailsPage.setHasFixedSize(true)
        getAllData { data: List<Article> ->
            RecomRecView_ProductDetailsPage.adapter = ProductAdapter(data,this)
        }

        val btnGenerateQRcode = findViewById<FloatingActionButton>(R.id.QrCodeBtn)
        btnGenerateQRcode.setOnClickListener {
            var data = name.toString() + ", " + price.toString() + ",, " + picture.toString() + ",,, " +
                    id.toString() + ",,,, " + details.toString() + ",,,,, " + type.toString()
            var dataa = listOf<String>(name.toString(),price.toString(), picture.toString(), id.toString(),
                details.toString(), type.toString())
            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for(x in 0 until width){
                    for(y in 0 until  height){
                        bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                scrollTest.isVisible = false
                ivQRcodeLayout.isVisible = true
                ivQRcode.setImageBitmap(bmp)
            }catch (e: WriterException){

            }
        }


        detailss.text = details
        namee.text = name
        pricee.text = price + " DT"
        Log.i("picture asba",picture.toString())
        Glide.with(applicationContext).load(Uri.parse(picture)).into(picturee)
        val refuser = mSharedPref.getString("UserID", null)
        val LoveBtn = findViewById<FloatingActionButton>(R.id.LoveBtn)
        val favoritt = Favorite()
        favoritt.name = name
        favoritt.price = price
        favoritt.favPicture = picture
        favoritt.refArticle = id
        favoritt.refuser = refuser
        val facturee = Facture()
        facturee.name = name
        facturee.price = price
        facturee.cartPicture = picture
        facturee.refArticle = id
        facturee.refuser = refuser

        LoveBtn.setOnClickListener {
            if(isChecked){
                LoveBtn.setImageResource(R.drawable.ic_heart_empty)
                isChecked = false
                val apiuserr = ApiUser.create().deleteFromFav(id.toString(), refuser.toString())
                apiuserr.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())
                            Toast.makeText(this@UserArticleDetails, "article deleted from favourite", Toast.LENGTH_SHORT).show()
                        } else {
                            println(response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            } else {
                LoveBtn.setImageResource(R.drawable.ic_heart_filled)
                isChecked = true
                val apiuser = ApiUser.create().addToFav(favoritt)
                apiuser.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())
                            println("refuser" + refuser)
                            println("refArticle" + id)
                            Toast.makeText(this@UserArticleDetails, "article added to favourite", Toast.LENGTH_SHORT).show()
                        } else {
                            println(response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
//        LoveBtn.setOnCheckedChangeListener { checkBox, isChecked ->
//            if (isChecked) {
//
//
//                val apiuser = ApiUser.create().addToFav(favoritt)
//                apiuser.enqueue(object : Callback<FavoriteResponse> {
//                    override fun onResponse(
//                        call: Call<FavoriteResponse>,
//                        response: Response<FavoriteResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            println(response.body().toString())
//                            println("refuser" + refuser)
//                            println("refArticle" + id)
//                        } else {
//                            println(response.body().toString())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
//                        TODO("Not yet implemented")
//                    }
//                })
//            } else {
//                println("wajcp")
//                val apiuserr = ApiUser.create().deleteFromFav(id.toString(), refuser.toString())
//                apiuserr.enqueue(object : Callback<FavoriteResponse> {
//                    override fun onResponse(
//                        call: Call<FavoriteResponse>,
//                        response: Response<FavoriteResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            println(response.body().toString())
//                        } else {
//                            println(response.body().toString())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
//                        TODO("Not yet implemented")
//                    }
//                })
//            }
//        }


        //test vottom sheet dialog
        val addTocart1 = findViewById<Button>(R.id.addArticleToCart)
        addTocart1.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                this@UserArticleDetails, R.style.BottomSheetDialogTheme
            )
            val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
                R.layout.layout_bottom_sheet,
                findViewById<LinearLayout>(R.id.bottomSheet)
            )
            //increase
            bottomSheetView.findViewById<View>(R.id.increase).setOnClickListener {
                val quantity = bottomSheetView.findViewById<TextView>(R.id.integer_number)
                num++
                quantity.text = num.toString()
            }
            //decrease
            bottomSheetView.findViewById<View>(R.id.decrease).setOnClickListener {
                val quantity = bottomSheetView.findViewById<TextView>(R.id.integer_number)
                if(num>1){
                    num--
                }

                quantity.text = num.toString()
            }
            bottomSheetView.findViewById<View>(R.id.addToCart).setOnClickListener {
                //Toast.makeText(this@UserArticleDetails, "article added to cart", Toast.LENGTH_SHORT).show()
                val quantity = bottomSheetView.findViewById<TextView>(R.id.integer_number)
                facturee.qte = quantity.text.toString()
                val newprice = price?.toInt()?.times(num)
                facturee.price = newprice.toString()
                val apiuser = ApiUser.create().addToCart(facturee)
                apiuser.enqueue(object : Callback<PostFacture> {
                    override fun onResponse(
                        call: Call<PostFacture>,
                        response: Response<PostFacture>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())
                            println("refuser" + refuser)
                            println("refArticle" + id)
                            Toast.makeText(this@UserArticleDetails, "article added to cart", Toast.LENGTH_SHORT).show()
                        } else {
                            println(response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<PostFacture>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

    }
    private fun getAllData(callback: (List<Article>) -> Unit){
        val apiInterface = ApiUser.create()

        apiInterface.getCompanyArticle(type).enqueue(object:
            Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }
        })
    }


}