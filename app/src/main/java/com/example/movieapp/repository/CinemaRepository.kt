package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.MovieEntity

interface CinemaRepository {

    fun getAllCinemas(): LiveData<List<Cinema>>

    fun getCinema(id: Int): LiveData<Cinema>

    // Movie
    fun insertMovie(movieEntity: MovieEntity)

    fun checkIfFavorite(movieId: Int): Boolean

    fun deleteFromFavorites(movieId: Int)
}
