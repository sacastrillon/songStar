package com.example.woo.songstar.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppUtils {

    companion object {
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}