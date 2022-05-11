package com.example.showapp.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Api.ApiUser
import com.example.showapp.R
import com.example.showapp.Activity.UserArticleList
import com.example.showapp.Model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.user_card_article_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserArticleListAdapter(private val userArticleList: List<Article>, private val listener: UserArticleList, val context: Context) :
    RecyclerView.Adapter<UserArticleListAdapter.MyViewHolder>() {

    lateinit var mSharedPref: SharedPreferences


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(property: Article, context: Context) {
            itemView.articleTitle.text = property.name
            itemView.articlePrice.text = property.price +" Dt"
            Glide.with(itemView).load(property.articlePicture).into(itemView.imageArticle)
            val addArticleToCartt = itemView.findViewById<FloatingActionButton>(R.id.addArticleToCartt)
            val LoveBtnn = itemView.findViewById<FloatingActionButton>(R.id.LoveBtnn)
            LoveBtnn.setOnClickListener {
                val favoritt = Favorite()
                favoritt.name = property.name
                favoritt.price = property.price
                favoritt.favPicture = property.articlePicture
                favoritt.refArticle = property._id
                mSharedPref = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                favoritt.refuser = mSharedPref.getString("UserID", null)
                val apiuser = ApiUser.create().addToFav(favoritt)
                apiuser.enqueue(object : Callback<FavoriteResponse> {
                    override fun onResponse(
                        call: Call<FavoriteResponse>,
                        response: Response<FavoriteResponse>
                    ) {
                        if (response.isSuccessful) {
                            println(response.body().toString())
                            Toast.makeText(context, "article added to favourit", Toast.LENGTH_SHORT).show()

                        } else {
                            println(response.body().toString())
                        }
                    }

                    override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
            addArticleToCartt.setOnClickListener {
//                val builder = AlertDialog.Builder(context)
//                builder.setTitle("Alert")
//                builder.setMessage("Are you sure you want to delete this article from favourit?")
//                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    val facturee = Facture()
                    facturee.qte = "1"
                    facturee.cartPicture = property.articlePicture
                    facturee.name = property.name
                    facturee.price = property.price
                    facturee.refArticle = property._id
                    mSharedPref = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
                    facturee.refuser = mSharedPref.getString("UserID", null)
                    val apiuser = ApiUser.create().addToCart(facturee)
                    apiuser.enqueue(object : Callback<PostFacture> {
                        override fun onResponse(
                            call: Call<PostFacture>,
                            response: Response<PostFacture>
                        ) {
                            if (response.isSuccessful) {
                                println(response.body().toString())
                                //println("refuser" + refuser)
                                //println("refArticle" + id)
                                Toast.makeText(context, "article added to cart", Toast.LENGTH_SHORT).show()
                            } else {
                                println(response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<PostFacture>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
//                }
//
//                builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                }
//                builder.show()
            }

        }
        init {
            itemView.setOnClickListener (this)
        }
        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position,userArticleList)
            }
        }



    }
    interface OnItemClickListener {
        fun onItemClick(position: Int,property: List<Article>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card_article_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userArticleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(userArticleList.get(position), context)

    }




}