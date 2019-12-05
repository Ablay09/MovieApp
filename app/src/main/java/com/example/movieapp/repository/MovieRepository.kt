package com.example.movieapp.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import io.reactivex.Single

interface MovieRepository {

    // Movie
    fun getPopularMovies(page: Int) : Single<MovieResponseData>

    suspend fun getMovieById(movieId: Int): MovieData?

    suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int): MovieResponseData?

    suspend fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite:Boolean): Int?
}