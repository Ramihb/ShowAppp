package com.example.showapp.Activity

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Facture
import com.example.showapp.Model.Favorite
import com.example.showapp.Model.FavoriteResponse
import com.example.showapp.Model.PostFacture
import com.example.showapp.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_user_article_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserArticleDetails : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    var num = 1
    var isChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_article_details)

        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        val name = intent.getStringExtra("articleName")
        val price = intent.getStringExtra("articlePrice")
        val picture = intent.getStringExtra("articlePicture")
        val id = intent.getStringExtra("id")
        val details = intent.getStringExtra("details")
        val type = intent.getStringExtra("type")

        val namee = findViewById<TextView>(R.id.NameArticleDetails)
        val pricee = findViewById<TextView>(R.id.PriceArticleDetails)
        val picturee = findViewById<ImageView>(R.id.ImageArticleDetails)
        val detailss = findViewById<TextView>(R.id.DetailArticleDetails)

        val test = findViewById<ImageView>(R.id.ImageArticleDetailss)
        collapsing_toolbar.title = name
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (Math.abs(verticalOffset)  >= appBarLayout.totalScrollRange) { // collapse
                collapsing_toolbar.title = "Category: " + type
                detailss.text = details
                namee.text = name
                pricee.text = price + " DT"
                Glide.with(applicationContext).load(Uri.parse(picture)).into(test)
            } else if (verticalOffset == 0) { // fully expand

            } else { // scolling
                test.setImageResource(0)
                detailss.text = null
                namee.text = "Scroll to find details"
                pricee.text = null
                collapsing_toolbar.title = name
            }
        })


//        detailss.text = details
//        namee.text = name
//        pricee.text = price + " DT"
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
        val addTocart1 = findViewById<FloatingActionButton>(R.id.addArticleToCart)
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


}