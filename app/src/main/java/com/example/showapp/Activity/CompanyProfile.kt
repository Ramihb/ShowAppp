package com.example.showapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.showapp.R

class CompanyProfile : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)

        val new = findViewById<TextView>(R.id.News)
        new.setOnClickListener{
            navigateToListNews()
        }


        val add = findViewById<TextView>(R.id.AddArticle)

        val brandPhoto = findViewById<ImageView>(R.id.CompanyImage)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)
        val brandPicture = mSharedPref.getString("brandPicCompany", null)
        Glide.with(applicationContext).load(Uri.parse(brandPicture)).into(brandPhoto)
        Log.i("testing the picture", brandPicture.toString())
        add.setOnClickListener {
            navigateToAddArticle()
        }
        val list = findViewById<TextView>(R.id.ArticleList)
        list.setOnClickListener {
            navigateToListArticle()
        }

        val logOutBtn = findViewById<Button>(R.id.LogOut)
        logOutBtn.setOnClickListener {
            mSharedPref.edit().clear().apply()
            navigateToLogin()
        }
    }

    private fun navigateToAddArticle() {
        finish()
        val intent = Intent(this@CompanyProfile, CompanyAddArticle::class.java )
        startActivity(intent)
    }
    private fun navigateToListArticle() {
        finish()
        val intent = Intent(this@CompanyProfile, CompanyArticleList::class.java )
        startActivity(intent)
    }
    private fun navigateToListNews() {
        finish()
        val intent = Intent(this@CompanyProfile, CompanyNewsList::class.java )
        startActivity(intent)
    }
    private fun navigateToLogin() {
        finish()
        val intent = Intent(this@CompanyProfile, MainActivity::class.java )
        startActivity(intent)
    }


}

