package com.example.showapp.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showapp.Adapter.AdminAdapter
import com.example.showapp.Api.ApiCompany
import com.example.showapp.Model.Company
import com.example.showapp.R
import kotlinx.android.synthetic.main.activity_admin_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        AdminRcv.layoutManager = LinearLayoutManager(this)
        AdminRcv.setHasFixedSize(true)
        getAllData { articless: List<Company> ->
            AdminRcv.adapter =
                applicationContext?.let { AdminAdapter(articless) }
        }

    }

    private fun getAllData(callback: (List<Company>) -> Unit){
        val apiInterface = ApiCompany.create()

        apiInterface.getAllCompanys().enqueue(object:
            Callback<Company> {
            override fun onResponse(call: Call<Company>, response: Response<Company>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.companys!!)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<Company>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }
        })
    }
}