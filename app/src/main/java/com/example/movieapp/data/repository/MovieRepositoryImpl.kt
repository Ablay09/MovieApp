package com.example.movieapp.data.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import com.example.movieapp.data.network.MovieApi
import com.example.movieapp.repository.MovieRepository
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    override fun getPopularMovies(page: Int): Single<MovieResponseData> =
        movieApi.getPopularMovies(page)

    override fun getFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int
    ): Single<MovieResponseData> =
        movieApi.getFavoriteMovies(accountId, sessionId, page)

    override fun rateMovie(
        movieId: Int,
        accountId: Int,
        sessionId: String,
        favorite: Boolean
    ): Observable<Int> {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favorite)
        }
        return movieApi.rateMovie(accountId, sessionId, body)
            .map { response -> response.code }
    }

    override fun getMovieById(movieId: Int): Single<MovieData> =
        movieApi.getMovie(movieId)

}