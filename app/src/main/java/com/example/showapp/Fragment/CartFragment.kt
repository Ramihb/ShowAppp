package com.example.showapp.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.showapp.Api.ApiUser
import com.example.showapp.Adapter.FactureViewAdapter
import com.example.showapp.Model.Facture
import com.example.showapp.Model.GetFactures
import com.example.showapp.R
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartFragment : Fragment() {
    lateinit var mSharedPrefUser: SharedPreferences
    lateinit var refuser: String
    lateinit var animationView: LottieAnimationView
    lateinit var fac: List<Facture>
    var sum:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_cart, container, false)
        mSharedPrefUser = this.requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        refuser = mSharedPrefUser.getString("UserID", null).toString()
        var totalPriceBagFrag = view.findViewById<TextView>(R.id.totalPriceBagFrag)

        animationView = view.findViewById(R.id.animationViewCartPage)
        val emptyBagMsgLayout: LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        animationView.playAnimation()
        animationView.loop(true)
        emptyBagMsgLayout.visibility = View.VISIBLE
        view.recycler_viewCart.layoutManager = LinearLayoutManager(activity)

        view.recycler_viewCart.setHasFixedSize(true)
        getFacData { fac -> fac?.let{
            if (fac.size == 0){
                Log.i("coucouc","cc")
                animationView.playAnimation()
                animationView.loop(true)
                emptyBagMsgLayout.visibility = View.VISIBLE
            }else{
                emptyBagMsgLayout.visibility = View.GONE
                animationView.pauseAnimation()
                view.recycler_viewCart.adapter = context?.let { FactureViewAdapter(fac, it) }
            }
        }
            fac.forEach {
                sum += it.price!!.toInt()
            }
            totalPriceBagFrag.text = sum.toString() + " DT"

        }
        /*view.swipeToRefreshCart.setOnRefreshListener {
            view.recycler_viewCart.layoutManager = LinearLayoutManager(activity)
            getFacData { fac ->
                view.recycler_viewCart.adapter = context?.let { FactureViewAdapter(fac, it) }
            }
            view.swipeToRefreshCart.isRefreshing = false
        }*/


        return view
    }
    private fun getFacData(callback: (List<Facture>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getFac(refuser).enqueue(object: Callback<GetFactures> {
            override fun onResponse(call: Call<GetFactures>, response: Response<GetFactures>) {
                if(response.isSuccessful){
                    return callback(response.body()!!.factures)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())                }
            }

            override fun onFailure(call: Call<GetFactures>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }

}