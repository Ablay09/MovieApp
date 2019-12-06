package com.example.movieapp.data.models

import com.google.gson.annotations.SerializedName

data class FavoriteResponseData (
    @SerializedName("status_code") val code: Int? = null,
    @SerializedName("status_message") val message: String? = null
)