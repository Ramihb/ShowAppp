package com.example.showapp.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Model.Order
import com.example.showapp.R
import kotlinx.android.synthetic.main.history_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val historyList : List<Order>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    val DayInMilliSec = 86400000
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(property: Order) {
            for(i in 0 until property.referenceFacture.size){
                itemView.ArticleName.text = "Name: " + property.referenceFacture[i].name
                itemView.ArticlePrice.text = "Price: " + property.referenceFacture[i].price
                itemView.ArticleQuantity.text = "Quantity: " + property.referenceFacture[i].qte
                itemView.ArticleDate.text = "Date: " + getDateTime(property.dateOrder!!)
                Glide.with(itemView).load(property.referenceFacture[i].cartPicture).into(itemView.ArticleImage)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(historyList.get(position))
    }
    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/YYYY")
            val netDate = Date(s.toLong()  ).addDays(1)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun Date.addDays(numberOfDaysToAdd: Int): Date {
        return Date(this.time + numberOfDaysToAdd * DayInMilliSec)
    }
}