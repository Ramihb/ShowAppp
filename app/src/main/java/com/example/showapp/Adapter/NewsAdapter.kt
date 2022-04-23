package com.example.showapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Model.New
import com.example.showapp.R
import kotlinx.android.synthetic.main.new_item.view.*

class NewsAdapter(private val newsList : List<New>): RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(property: New) {
            Glide.with(itemView).load(property.newsPicture).into(itemView.NewsImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.new_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(newsList.get(position))
    }
}