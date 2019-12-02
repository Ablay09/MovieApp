package com.example.movieapp.data.network

import com.example.movieapp.data.models.AccountData
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface MovieApi {

    // Login
    @POST("authentication/token/validate_with_login")
    fun login(@Body body: JsonObject): Deferred<Response<JsonObject>>


    @POST("authentication/session/new")
    fun createSession(@Body body: JsonObject) : Deferred<Response<JsonObject>>

    @GET("authentication/token/new")
    fun createRequestToken(): Deferred<Response<JsonObject>>


    // Account
    @GET("account")
    fun getAccountId(@Query("session_id") sessionId: String): Deferred<Response<AccountData>>


    // Movie
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Deferred<Response<MovieResponseData>>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ) : Deferred<Response<MovieResponseData>>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int): Deferred<Response<MovieData>>

    @POST("account/{account_id}/favorite")
    fun rateMovie(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body body: JsonObject
    ) : Deferred<Response<JsonObject>>
}