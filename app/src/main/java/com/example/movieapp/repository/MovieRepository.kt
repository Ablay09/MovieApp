package com.example.movieapp.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import io.reactivex.Observable
import io.reactivex.Single

interface MovieRepository {

    // Movie
    fun getPopularMovies(page: Int): Single<MovieResponseData>

    fun getTopRatedMovies(page: Int): Single<MovieResponseData>

    fun getUpcomingMovies(page: Int): Single<MovieResponseData>

    fun getMovieById(movieId: Int): Single<MovieData>

    fun getFavoriteMovies(sessionId: String, page: Int): Single<MovieResponseData>

    fun favMovie(movieId: Int, accountId: Int, sessionId: String, favorite:Boolean): Observable<Int>
}