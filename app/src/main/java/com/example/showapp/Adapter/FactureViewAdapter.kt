package com.example.showapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Api.ApiUser
import com.example.showapp.Model.*
import com.example.showapp.R
import kotlinx.android.synthetic.main.cart_item.view.*
import kotlinx.android.synthetic.main.fav_item.view.FavImage
import kotlinx.android.synthetic.main.fav_item.view.favPrice
import kotlinx.android.synthetic.main.fav_item.view.favTitle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FactureViewAdapter(private val facList : List<Facture>,val context: Context): RecyclerView.Adapter<FactureViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var refuser: String
        lateinit var id:String
        fun bind(property: Facture, context: Context) {
            itemView.favTitle.text = "Name: " + property.name
            itemView.favPrice.text = "Price: " + property.price
            itemView.cartQuantity.text = "Quantity: " + property.qte
            Glide.with(itemView).load(property.cartPicture).into(itemView.FavImage)
            //itemView.LoveBtn.setBackgroundColor(R.drawable.ic_heart_filled)
            refuser = property.refuser.toString()
            id = property._id.toString()
            var deleteBtn = itemView.findViewById<ImageView>(R.id.DeleteBtn)
            deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Alert")
                builder.setMessage("Are you sure you want to delete this article from your cart?")
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    val apiuserr = ApiUser.create().deleteFromFac(id,refuser)
                    apiuserr.enqueue(object: Callback<PostFacture> {
                        override fun onResponse(
                            call: Call<PostFacture>,
                            response: Response<PostFacture>
                        ) {
                            if(response.isSuccessful){
                                println(response.body().toString())
                            } else{
                                println(response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<PostFacture>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }
                builder.show()


            }
            }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(facList.get(position),context)
    }

    override fun getItemCount(): Int {
        return facList.size
    }
}