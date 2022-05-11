package com.example.showapp.Model

data class BillModelPDF (
    var ArticleName : String,
    var Quantity: String,
    var Price: String,
    var Total: String,
    val PositionAndCityName: String,
)
