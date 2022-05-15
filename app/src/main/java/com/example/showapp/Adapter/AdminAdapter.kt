package com.example.showapp.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.showapp.Model.Company
import com.example.showapp.R
import kotlinx.android.synthetic.main.admin_item.view.*

class AdminAdapter(private val companyList : List<Company>): RecyclerView.Adapter<AdminAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(property: Company) {

            itemView.ArticleName.text = "Name: " + property.businessNameCompany
            if(property.verifiedCompany == true){
                itemView.ArticleName.setTextColor(Color.parseColor("#2E8B57"))
            } else {
                itemView.ArticleName.setTextColor(Color.parseColor("#DC143C"))
            }
            itemView.ArticlePrice.text = "Price: " + property.categoryCompany
            itemView.ArticleQuantity.text = "Number: " + property.phoneNumberCompany
            Glide.with(itemView).load(property.brandPicCompany).into(itemView.ArticleImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(companyList.get(position))
    }
}