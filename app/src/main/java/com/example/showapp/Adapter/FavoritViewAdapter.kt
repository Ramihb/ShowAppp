package com.example.showapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.Facture
import com.example.showapp.Model.Favorite
import com.example.showapp.Model.FavoriteResponse
import com.example.showapp.R
import kotlinx.android.synthetic.main.fav_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritViewAdapter(private val favList : MutableList<Favorite>,val context: Context): RecyclerView.Adapter<FavoritViewAdapter.ViewHolder>() {
    lateinit var refuser: String
    lateinit var id:String
    class ViewHolder(view : View): RecyclerView.ViewHolder(view){



        fun bind(property: Favorite, context: Context){
            itemView.favTitle.text = property.name
            itemView.favPrice.text = property.price + " DT"
            Glide.with(itemView).load(property.favPicture).into(itemView.FavImage)

            var addArticleToCart = itemView.findViewById<Button>(R.id.addArticleToCart)
            addArticleToCart.setOnClickListener {
                val facturee = Facture()
                facturee.qte = "1"
                facturee.cartPicture = property.favPicture
                facturee.name = property.name
                facturee.price = property.price
                facturee.refArticle = property.refArticle
                facturee.refuser = property.refuser
                val apiuser = ApiUser.create().addToCart(facturee)
                apiuser.enqueue(object : Callback<Facture> {
                    override fun onResponse(
                        call: Call<Facture>,
                        response: Response<Facture>
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

                    override fun onFailure(call: Call<Facture>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

            }
        }

    }
    fun deleteItem(i: Int){
        refuser = favList[i].refuser.toString()
        id = favList[i].refArticle.toString()

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Are you sure you want to delete this article from favourit?")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val apiuserr = ApiUser.create().deleteFromFav(id,refuser)
            apiuserr.enqueue(object: Callback<FavoriteResponse> {
                override fun onResponse(
                    call: Call<FavoriteResponse>,
                    response: Response<FavoriteResponse>
                ) {
                    if(response.isSuccessful){
                        println(response.body().toString())
                        favList.removeAt(i)
                    } else{
                        println(response.body().toString())
                    }
                }

                override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
        }
        builder.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favList.get(position),context)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

}
