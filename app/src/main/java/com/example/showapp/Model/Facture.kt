package com.example.showapp.Model

data class Facture(
    var _id: String? = null,
    var refArticle: String? = null,
    var refuser: String? = null,
    var name: String? = null,
    var price: String? = null,
    var cartPicture: String? = null,
    var qte: String? = null,
)

data class GetFactures(
    val factures: List<Facture>
)

data class PostFacture(
    var message: String? = null
)