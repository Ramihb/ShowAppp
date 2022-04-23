package com.example.showapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Model.Article
import com.example.showapp.R
import kotlinx.android.synthetic.main.row_item.view.*

class MyAdapter(private val companyList : List<Article>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    class MyViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bind(property: Article){
            itemView.ArticleName.text = property.name
            itemView.ArticlePrice.text = property.price
            Glide.with(itemView).load(property.articlePicture).into(itemView.ArticleImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false))
    }
    override fun getItemCount(): Int {
        return companyList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(companyList.get(position))
    }


}

