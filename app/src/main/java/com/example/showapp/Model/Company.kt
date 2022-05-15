package com.example.showapp.Model


data class Company(
    var _id: String? = null,
    var emailCompany: String? = null,
    var passwordCompany: String? = null,
    var phoneNumberCompany: Int? = null,
    var lastNameCompany: String? = null,
    var firstNameCompany: String? = null,
    var categoryCompany: String? = null,
    var brandPicCompany: String? = null,
    var businessNameCompany: String? = null,
    var verifiedCompany: Boolean? = null,
    var __v:Int? = 0,
    val Company : Company? = null,
    val token : String? = null,
    val message : String? = null,
    val error: String? = null,
    val companys: ArrayList<Company> = arrayListOf(),
)

