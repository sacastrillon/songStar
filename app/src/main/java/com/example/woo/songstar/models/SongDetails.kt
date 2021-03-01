package com.example.woo.songstar.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SongDetails (
    @SerializedName("artistName") var artistName: String,
    @SerializedName("trackName") var trackName: String,
    @SerializedName("primaryGenreName") var gender: String,
    @SerializedName("collectionName") var album: String,
    @SerializedName("artworkUrl100") var image: String
): Serializable