package com.example.showapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showapp.Activity.UserActivity
import com.example.showapp.Activity.UserArticleDetails
import com.example.showapp.Activity.UserArticleList
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.New
import com.example.showapp.Adapter.NewsViewAdapter
import com.example.showapp.Adapter.ProductAdapter
import com.example.showapp.Model.Article
import com.example.showapp.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    var test: MutableList<String> = ArrayList()
    lateinit var newRecView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        newRecView = view.findViewById(R.id.newRecView)

        view.recycler_viewNews.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        view.recycler_viewNews.setHasFixedSize(true)
        getNewsData { newss: List<New> ->
            view.recycler_viewNews.adapter = NewsViewAdapter(newss)
        }
        newRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        newRecView.setHasFixedSize(true)
        getArticlesData { data: List<Article> ->
            view.newRecView.adapter = ProductAdapter(data,requireContext())
        }

        val viewAllBtn = view.findViewById<TextView>(R.id.product_GroupViewAll)
        viewAllBtn.setOnClickListener {
            val intent = Intent(requireContext(), UserArticleList::class.java)
            intent.putExtra("type","null")
            navigateToAllArticles()
        }

        return view
    }

    private fun getNewsData(callback: (List<New>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getCompanyNews().enqueue(object : Callback<New> {
            override fun onResponse(call: Call<New>, response: Response<New>) {
                if (response.isSuccessful) {
                    return callback(response.body()!!.news)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<New>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
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
    private fun navigateToAllArticles() {
        val intent = Intent(requireContext(), UserArticleList::class.java)
        startActivity(intent)
    }

}