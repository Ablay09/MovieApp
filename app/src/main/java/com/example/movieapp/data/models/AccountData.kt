package com.example.movieapp.data.models

import com.google.gson.annotations.SerializedName

data class AccountData (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("include_adult") val adult: Boolean? = null,
    @SerializedName("username") val username: String? = null
)