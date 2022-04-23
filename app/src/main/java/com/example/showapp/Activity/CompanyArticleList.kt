package com.example.showapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showapp.Adapter.MyAdapter
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.Article
import com.example.showapp.R
import kotlinx.android.synthetic.main.activity_company_article_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyArticleList : AppCompatActivity() {
    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_article_list)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        getAllData{ articless : List<Article> ->
            recycler_view.adapter = MyAdapter(articless)
        }


    }
    private fun getAllData(callback: (List<Article>) -> Unit){
        val apiInterface = ApiCompany.create()
        apiInterface.getCompanyArticle(mSharedPref.getString("brandID", null)).enqueue(object: Callback<Article> {
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
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@CompanyArticleList, CompanyProfile::class.java)
        startActivity(intent)
    }
}