package com.example.showapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showapp.Adapter.NewsAdapter
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.New
import com.example.showapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_company_article_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyNewsList : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_news_list)
        mSharedPref = getSharedPreferences("CompanyPref", Context.MODE_PRIVATE)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        getAllData{ newss : List<New> ->
            recycler_view.adapter = NewsAdapter(newss)
        }

        val addNewsBtn = findViewById<TextView>(R.id.addNews)
        addNewsBtn.setOnClickListener {
            navigateToAddNews()
        }
    }
    private fun getAllData(callback: (List<New>) -> Unit){
        val apiInterface = ApiCompany.create()
        apiInterface.getCompanyNews(mSharedPref.getString("brandID", null)).enqueue(object:
            Callback<New> {
            override fun onResponse(call: Call<New>, response: Response<New>) {
                if(response.isSuccessful){
                    if(response.body()!!.news!!.isEmpty()){
                        showAlertDialog()
                    }
                    return callback(response.body()!!.news)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<New>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }
        })
    }
    private fun navigateToAddNews() {
        finish()
        val intent = Intent(this@CompanyNewsList, CompanyAddNews::class.java )
        startActivity(intent)
    }
    //begin Alert dialog
    private fun showAlertDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("Sorry there is no article yet")
            .setPositiveButton("Ok") {dialog, which ->
                Toast.makeText(this,"no news yet",Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    //end Alert dialog
        
    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(this@CompanyNewsList, CompanyProfile::class.java)
        startActivity(intent)
    }
}