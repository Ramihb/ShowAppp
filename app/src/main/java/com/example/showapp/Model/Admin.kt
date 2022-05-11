package com.example.showapp.Model

data class Admin(
    var _id: String?= null,
    var email: String?= null,
    var password: String?= null,
    var __v:Int? = 0,
    val user : Admin? = null,
    val token : String? = null,
)
