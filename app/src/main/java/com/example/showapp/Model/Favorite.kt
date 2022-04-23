package com.example.showapp.Model

data class Favorite(
    var name: String? = null,
    var price: String? = null,
    var refArticle: String? = null,
    var refuser: String? = null,
    var favPicture: String? = null
)

data class FavoriteResponse (
    val message: String? = null
        )
data class FavoriteRefuser (
    val favorites: List<Favorite>
        )
