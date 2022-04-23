package com.example.showapp.Model

data class Article (
    var _id : String?= null,
    var brand: String?= null,
    var name: String?= null,
    var category: String?= null,
    var price : String?= null,
    var articlePicture : String?= null,
    var quantity:Int?=null,
    var type: String?= null,
    var __v:Int?=null,
    var Details: String?= null,
    var articles : List<Article>?= null,
    var message: String?= null
)

