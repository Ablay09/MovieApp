package com.example.movieapp.repository

import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.models.MovieResponseData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MovieRepository {

    // Movie
    fun getPopularMovies(page: Int) : Single<MovieResponseData>

    fun getMovieById(movieId: Int): Single<MovieData>

    fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int): Single<MovieResponseData>

    fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite:Boolean): Observable<Int>
}