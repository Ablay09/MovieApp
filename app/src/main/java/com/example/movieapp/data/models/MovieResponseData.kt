package com.example.movieapp.data.models


import com.google.gson.annotations.SerializedName

data class MovieResponseData (
    @SerializedName("page") val page: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?,
    @SerializedName("results") val results: List<MovieData>?
)