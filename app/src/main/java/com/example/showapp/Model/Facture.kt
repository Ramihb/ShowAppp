package com.example.showapp.Model

data class Facture(
    var _id: String? = null,
    var refArticle: String? = null,
    var refuser: String? = null,
    var name: String? = null,
    var price: String? = null,
    var cartPicture: String? = null,
    var qte: String? = null,
    var show: Boolean? = null,
    val factures: List<Facture>?= null,
    var message: String? = null
)