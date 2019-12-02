package com.example.movieapp.data.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import com.example.movieapp.data.network.ApiClient
import com.example.movieapp.data.network.MovieApi
import com.example.movieapp.domain.repository.MovieRepository
import com.google.gson.JsonObject

class MovieRepositoryImpl: MovieRepository {

    //Movie
    override suspend fun getPopularMovies(page: Int) =
        ApiClient.apiClient.getPopularMovies(page).await().body()

    override suspend fun getFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int
    ): MovieResponseData? =
        ApiClient.apiClient.getFavoriteMovies(accountId, sessionId, page).await().body()

    override suspend fun getMovieById(movieId: Int): MovieData? =
        ApiClient.apiClient.getMovie(movieId).await().body()

    override suspend fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite: Boolean): Int? {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favorite)
        }
        val response = ApiClient.apiClient.rateMovie(accountId, sessionId, body).await()
        return response.body()?.getAsJsonPrimitive("status_code")?.asInt
    }
}