package com.example.woo.songstar.models

import com.google.gson.annotations.SerializedName

data class ItunesAPIResponse (
    @SerializedName("results") var results: ArrayList<SongDetails>
)