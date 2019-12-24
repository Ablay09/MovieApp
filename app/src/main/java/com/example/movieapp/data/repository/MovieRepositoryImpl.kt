package com.example.movieapp.data.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import com.example.movieapp.data.network.MovieApi
import com.example.movieapp.repository.MovieRepository
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    //Movie
    override fun getPopularMovies(page: Int): Single<MovieResponseData> =
        movieApi.getPopularMovies(page)

    override fun getTopRatedMovies(page: Int): Single<MovieResponseData> =
        movieApi.getTopRatedMovies(page)

    override fun getUpcomingMovies(page: Int): Single<MovieResponseData> =
        movieApi.getUpcomingMovies(page)

    override fun getFavoriteMovies(
        sessionId: String,
        page: Int
    ): Single<MovieResponseData> =
        movieApi.getFavoriteMovies(sessionId, page)


    override fun favMovie(
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
        return movieApi.favMovie(accountId, sessionId, body).map {
            response -> response.code
        }
    }

    override fun getMovieById(movieId: Int): Single<MovieData> =
        movieApi.getMovie(movieId)
}