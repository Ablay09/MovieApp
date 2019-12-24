package com.example.movieapp.data.network

import com.example.movieapp.data.models.AccountData
import com.example.movieapp.data.models.FavoriteResponseData
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
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
    fun getAccountDetails(@Query("session_id") sessionId: String): Observable<AccountData>


    // Movie
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Single<MovieResponseData>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Single<MovieResponseData>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MovieResponseData>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ) : Single<MovieResponseData>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int): Single<MovieData>

    @POST("account/{account_id}/favorite")
    fun favMovie(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body body: JsonObject
    ) : Observable<FavoriteResponseData>
}