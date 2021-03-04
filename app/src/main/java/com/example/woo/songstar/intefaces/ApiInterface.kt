package com.example.woo.songstar.intefaces

import com.example.woo.songstar.models.ItunesAPIResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("search")
    fun search(@Query("term", encoded = true) term: String): Call<ItunesAPIResponse>

}