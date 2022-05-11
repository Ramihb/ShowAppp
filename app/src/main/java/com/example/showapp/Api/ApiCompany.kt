package com.example.showapp.Api

import com.example.showapp.Model.Article
import com.example.showapp.Model.Company
import com.example.showapp.Model.New

import com.example.showapp.Utils.Constants.Companion.BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiCompany{


    //getting company articles
    @GET("articles/company/{id}")
    fun getCompanyArticle(@Path("id") id: String?): Call<Article>

    //login company
    @POST("company/login")
    fun logincompany(@Body company: Company) : Call<Company>

    //posting article
    @Multipart
    @POST("/articles")
    fun addArticle(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part articlePicture: MultipartBody.Part,


    ) : Call<Article>

    //signup company
    @Multipart
    @POST("/company/signup")
    fun companySignUp(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part brandPicCompany: MultipartBody.Part
    ) : Call<Company>

    //add news
    @Multipart
    @POST("/news")
    fun addNews(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part newsPicture: MultipartBody.Part
    ) : Call<New>

    //get news by id
    @GET("/news/brand/{id}")
    fun getCompanyNews(@Path("id") id: String?) : Call<New>

    //get all companys
    @GET("/company")
    fun getAllCompanys() : Call<Company>


    companion object {
        fun create() : ApiCompany {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiCompany::class.java)

        }
    }

}


