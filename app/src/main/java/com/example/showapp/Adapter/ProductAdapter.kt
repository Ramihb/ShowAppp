package com.example.showapp.Adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.showapp.Activity.UserArticleDetails
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Article
import com.example.showapp.Model.Favorite
import com.example.showapp.Model.FavoriteRefuser
import com.example.showapp.Model.FavoriteResponse
import com.example.showapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class ProductAdapter(private val articleList: List<Article>, context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    val ctx: Context = context
    lateinit var mSharedPref: SharedPreferences
    lateinit var test: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {

        val productView =
            LayoutInflater.from(parent.context).inflate(R.layout.single_product, parent, false)
        return ViewHolder(productView)
    }


    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val product: Article = articleList[position]
        mSharedPref = ctx.getSharedPreferences("UserPref", Context.MODE_PRIVATE)


        //holder.productRating_singleProduct.rating = product.productRating

        Glide.with(ctx)
            .load(product.articlePicture)
            .into(holder.productImage_singleProduct)

        holder.itemView.setOnClickListener {
            goDetailsPage(position)
        }
        holder.productType_singleProduct.text = product.type
        holder.productName_singleProduct.text = product.name
        holder.productPrice_singleProduct.text = product.price + " DT"
        var isChecked = false
        val favoritt = Favorite()
        favoritt.name = product.name
        favoritt.price = product.price
        favoritt.favPicture = product.articlePicture
        favoritt.refArticle = product._id
        favoritt.refuser = mSharedPref.getString("UserID", null)

        holder.productAddToFav_singleProduct.setOnClickListener {
            if (isChecked) {
                isChecked = false

                holder.dislikeMsgLayout.visibility = View.VISIBLE
                holder.animationViewDislike.setMinAndMaxProgress(0.0f, 1f)
                holder.animationViewDislike.playAnimation()

                val apiuserr = ApiUser.create().deleteFromFav(product._id.toString(), mSharedPref.getString("UserID", null).toString())
                apiuserr.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())

                            holder.dislikeMsgLayout.visibility = View.GONE
                            holder.animationViewDislike.pauseAnimation()
                            holder.productAddToFav_singleProduct.setImageResource(R.drawable.ic_heart_empty)
                            Toast.makeText(ctx,"article removed from favourit",Toast.LENGTH_LONG).show()
                        } else {
                            println(response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })


            }else {
                isChecked = true
                holder.heartMsgLayout.visibility = View.VISIBLE
                holder.animationView.setMinAndMaxProgress(0.0f, 1f)
                holder.animationView.playAnimation()
                val apiuser = ApiUser.create().addToFav(favoritt)
                apiuser.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())
                            holder.heartMsgLayout.visibility = View.GONE
                            holder.animationView.pauseAnimation()
                            holder.productAddToFav_singleProduct.setImageResource(R.drawable.ic_heart_filled)
                            Toast.makeText(ctx,"article added to favourit",Toast.LENGTH_LONG).show()

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

    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productImage_singleProduct: ImageView =
            itemView.findViewById(R.id.productImage_singleProduct)
        val productAddToFav_singleProduct: ImageView =
            itemView.findViewById(R.id.productAddToFav_singleProduct)
        val productRating_singleProduct: RatingBar =
            itemView.findViewById(R.id.productRating_singleProduct)
        val productType_singleProduct: TextView =
            itemView.findViewById(R.id.productType_singleProduct)
        val productName_singleProduct: TextView =
            itemView.findViewById(R.id.productName_singleProduct)
        val productPrice_singleProduct: TextView =
            itemView.findViewById(R.id.productPrice_singleProduct)
        val heartMsgLayout: LinearLayout = itemView.findViewById(R.id.HeartMsgLayout)
        var animationView: LottieAnimationView = itemView.findViewById(R.id.animationViewHeart)

        val dislikeMsgLayout: LinearLayout = itemView.findViewById(R.id.dislikeMsgLayout)
        var animationViewDislike: LottieAnimationView = itemView.findViewById(R.id.animationViewDislike)

    }

    private fun goDetailsPage(position: Int) {
        val intent = Intent(ctx, UserArticleDetails::class.java)
        intent.putExtra("articleName", articleList[position].name)
        intent.putExtra("articlePrice", articleList[position].price)
        intent.putExtra("articlePicture", articleList[position].articlePicture)
        intent.putExtra("id", articleList[position]._id)
        intent.putExtra("details", articleList[position].Details)
        intent.putExtra("type", articleList[position].type)
        ctx.startActivity(intent)
    }
    private fun getFavData(callback: (List<Favorite>) -> Unit) {
        val apiInterface = ApiUser.create()
        apiInterface.getFav(mSharedPref.getString("UserID", null).toString()).enqueue(object : Callback<FavoriteRefuser> {
            override fun onResponse(
                call: Call<FavoriteRefuser>,
                response: Response<FavoriteRefuser>
            ) {
                if (response.isSuccessful) {
                    for (i in 1..response.body()!!.favorites.count()) {
                        var testt = response.body()!!.favorites[i].refArticle
                        println("I love this article : $testt")
                    }
                    return callback(response.body()!!.favorites)
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