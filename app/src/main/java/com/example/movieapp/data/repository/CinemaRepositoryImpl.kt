package com.example.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.CinemaDao
import com.example.movieapp.data.room.MovieEntity
import com.example.movieapp.repository.CinemaRepository

class CinemaRepositoryImpl(private val cinemaDao: CinemaDao) : CinemaRepository {

    override fun getAllCinemas(): LiveData<List<Cinema>> = cinemaDao.getCinemas()

    override fun getCinema(id: Int): LiveData<Cinema> = cinemaDao.getCinema(id)

    // Favorite Movie
    override fun insertMovie(movieEntity: MovieEntity) = cinemaDao.insertMovie(movieEntity)

    override fun checkIfFavorite(movieId: Int): Boolean = cinemaDao.checkIfMovieIsFavorite(movieId)

    override fun deleteFromFavorites(movieId: Int) = cinemaDao.deleteFromFavorites(movieId)
}