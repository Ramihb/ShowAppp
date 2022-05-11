package com.example.showapp.Model

data class User (
    var _id:String? = null,
    var email: String? = null,
    var password: String? = null,
    var phoneNumber: String? = null,
    var profilePicture:String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var verified: Boolean? = null,
    var __v:Int? = 0,
    val user : User? = null,
    val token : String? = null,
    val reponse : String? = null,
    var code: Int?= null,
    val message:String? = null,
    val newsLettre:Boolean? = null
        )

