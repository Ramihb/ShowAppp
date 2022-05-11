package com.example.showapp.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.showapp.Api.ApiUser
import com.example.showapp.Adapter.FavoritViewAdapter
import com.example.showapp.Model.Favorite
import com.example.showapp.Model.FavoriteRefuser
import com.example.showapp.R
import com.example.showapp.Utils.SwipeGesture
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavouriteFragment : Fragment() {

    lateinit var mSharedPrefUser: SharedPreferences
    lateinit var refuser: String
    lateinit var id:String
    lateinit var animationView: LottieAnimationView
    lateinit var favvv: MutableList<Favorite>
    lateinit var favoritViewAdapter: FavoritViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        mSharedPrefUser = this.requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        refuser = mSharedPrefUser.getString("UserID", null).toString()
        id = requireActivity().intent.getStringExtra("id").toString()
        animationView = view.findViewById(R.id.animationViewFavPage)
        val emptyBagMsgLayout: LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        animationView.playAnimation()
        animationView.loop(true)
        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            favoritViewAdapter = context?.let { FavoritViewAdapter(favvv, it) }!!
            if (favvv.size == 0){
                animationView.playAnimation()
                animationView.loop(true)
                emptyBagMsgLayout.visibility = View.VISIBLE
            }
            Toast.makeText(requireContext(), "Data Refreshed", Toast.LENGTH_SHORT).show()
        }

        view.recycler_viewFavorit.layoutManager = LinearLayoutManager(activity)
        view.recycler_viewFavorit.setHasFixedSize(true)
        getFavData { favv ->
            favv.let{
                if (favv.size == 0){
                    Log.i("coucouc","cc")
                    animationView.playAnimation()
                    animationView.loop(true)
                    emptyBagMsgLayout.visibility = View.VISIBLE
                }else{
                    emptyBagMsgLayout.visibility = View.GONE
                    animationView.pauseAnimation()
                    favvv = favv
                    favoritViewAdapter = context?.let { FavoritViewAdapter(favv, it) }!!
                    view.recycler_viewFavorit.adapter = context?.let { FavoritViewAdapter(favv, it) }
                }
            }
        }
        val swipegesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        favoritViewAdapter.deleteItem(viewHolder.adapterPosition)
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(view.recycler_viewFavorit)


        return view
    }

    private fun getFavData(callback: (MutableList<Favorite>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getFav(refuser).enqueue(object : Callback<FavoriteRefuser> {
            override fun onResponse(
                call: Call<FavoriteRefuser>,
                response: Response<FavoriteRefuser>
            ) {
                if (response.isSuccessful) {
                    return callback(response.body()!!.favorites)
                    Log.i("yessss", response.body().toString())
                    //}
                } else {
                    Log.i("nooooo", response.body().toString())
                }
            }

            override fun onFailure(call: Call<FavoriteRefuser>, t: Throwable) {
                t.printStackTrace()
                println("OnFailure")
            }

        })
    }




}