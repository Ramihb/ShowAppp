package com.example.showapp.Model

data class New(
    var _id:String,
    var BrandsName:String?,
    var title:String?,
    var newsPicture:String?,
    var __v:Int,
    var news : List<New>,
    var message: String
)
