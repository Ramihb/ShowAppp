package com.example.showapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.showapp.Adapter.UserArticleListAdapter
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Article
import com.example.showapp.R
import kotlinx.android.synthetic.main.activity_user_article_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserArticleList : AppCompatActivity(), UserArticleListAdapter.OnItemClickListener {

    var type :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_article_list)
        type = intent.getStringExtra("type").toString()


        recycler_viewArticleList.layoutManager = GridLayoutManager(applicationContext, 2)
        recycler_viewArticleList.setHasFixedSize(true)


        if(type=="null") {

            getArticlesData { articless: List<Article> ->
                recycler_viewArticleList.adapter =
                    applicationContext?.let { UserArticleListAdapter(articless, this, it) }
            }


        } else {

            getAllData { articless: List<Article> ->
                type = intent.getStringExtra("type").toString()
                recycler_viewArticleList.adapter =
                    applicationContext?.let { UserArticleListAdapter(articless, this, it) }
            }
        }
    }
    private fun getArticlesData(callback: (List<Article>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getAllArticles().enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                if (response.isSuccessful) {
                    return callback(response.body()!!.articles!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
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
   /* override fun onBackPressed() {
        //super.onBackPressed()
        finish()
        val intent = Intent(applicationContext, UserActivity::class.java)
        startActivity(intent)
    }*/

    override fun onItemClick(position: Int,articless: List<Article>) {
        val intent = Intent(this@UserArticleList, UserArticleDetails::class.java)
        intent.putExtra("articleName",articless[position].name)
        intent.putExtra("articlePrice",articless[position].price)
        intent.putExtra("articlePicture",articless[position].articlePicture)
        intent.putExtra("id",articless[position]._id)
        intent.putExtra("details",articless[position].Details)
        intent.putExtra("type",articless[position].type)
        startActivity(intent)
    }


}