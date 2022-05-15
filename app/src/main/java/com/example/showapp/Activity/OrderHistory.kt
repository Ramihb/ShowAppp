package com.example.showapp.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showapp.Adapter.HistoryAdapter
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Order
import com.example.showapp.R
import kotlinx.android.synthetic.main.activity_order_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistory : AppCompatActivity() {

    lateinit var mSharedPref: SharedPreferences
    lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        mSharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        userId = mSharedPref.getString("UserID", null).toString()

        hisotryRcv.layoutManager = LinearLayoutManager(this)
        hisotryRcv.setHasFixedSize(true)
        getAllData { articless: List<Order> ->
            hisotryRcv.adapter =
                applicationContext?.let { HistoryAdapter(articless) }
        }

    }
    private fun getAllData(callback: (List<Order>) -> Unit){
        val apiInterface = ApiUser.create()

        apiInterface.getOrdersByUserId(userId).enqueue(object:
            Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.orders!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }
        })
    }
}