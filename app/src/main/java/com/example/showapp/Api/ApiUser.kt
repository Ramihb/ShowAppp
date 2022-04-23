package com.example.showapp.Api

import com.example.showapp.Model.*
import com.example.showapp.Utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiUser {

    //Sign up user
    @Multipart
    @POST("users/signup")
    fun userSignUp(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part?
    ) : Call<User>


    //Sign up user
    @Multipart
    @POST("users/signupMedia")
    fun userSignUpSocial(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part?
    ) : Call<User>

    //login user
    @POST("users/login")
    fun loginUser(@Body user: User): Call<User>

    //login social user
    @POST("users/loginSocial")
    fun loginSocial(@Body user: User): Call<User>

    //getting company news
    @GET("news")
    fun getCompanyNews(): Call<New>

    //getting articles by name
    @GET("/articles/type/{type}")
    fun getCompanyArticle(@Path("type") type: String?): Call<Article>

    //add to favourit
    @POST("/favorites/add")
    fun addToFav(@Body favorit: Favorite): Call<FavoriteResponse>
    //delete from cart
    @DELETE("factures/{refArticle}/{refuser}")
    fun deleteFromFac(@Path("refArticle")refArticle: String?,@Path("refuser") refuser: String?) : Call<PostFacture>
    //delete from favorit
    @DELETE("favorites/{refArticle}/{refuser}")
    fun deleteFromFav(@Path("refArticle")refArticle: String?,@Path("refuser") refuser: String?) : Call<FavoriteResponse>
    //get favorit
    @GET("/favorites/refuser/{refuser}")
    fun getFav(@Path("refuser")refuser: String?): Call<FavoriteRefuser>

    @GET("/factures/refuser/{refuser}")
    fun getFac(@Path("refuser")refuser: String?): Call<GetFactures>

    @POST("factures/add")
    fun addToCart(@Body facture: Facture): Call<PostFacture>

    //Forget password1
    @POST("/users/reset")
    fun forgetPassword(@Body user: User): Call<Void>
    //Forget password2
    @PATCH("users/reset")
    fun resetPassword(@Body forgetPassword: User): Call<User>
    @GET("/articles")
    fun getAllArticles(): Call<Article>
    @Multipart
    @PUT("users/{refuser}")
    fun editMail(@Path("refuser") refuser: String?,
        @PartMap data : LinkedHashMap<String, RequestBody>
    ) : Call<User>

    @Multipart
    @PUT("users/{refuser}")
    fun editPicture(@Path("refuser") refuser: String?,
                    @Part profilePicture: MultipartBody.Part
    ) : Call<User>



    companion object {
        fun create() : ApiUser {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
            return retrofit.create(ApiUser::class.java)

        }
    }
}