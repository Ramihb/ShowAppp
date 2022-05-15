package com.example.showapp.Model

data class Order(
    var _id: String? = null,
    //var referenceFacture: MutableList<Facture>? = null,
    var referenceFacture : ArrayList<Facture> = arrayListOf(),
    var userId: String? = null,
    var dateOrder: String?= null,
    var message: String? = null,
    val orders: List<Order>?=null,
    var listString: ArrayList<String> = arrayListOf()
)
