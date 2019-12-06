package com.example.movieapp.data.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.CinemaDao
import com.example.movieapp.domain.repository.CinemaRepository

class CinemaRepositoryImpl(private val cinemaDao: CinemaDao) : CinemaRepository {

    override fun getAllCinemas(): LiveData<List<Cinema>> = cinemaDao.getCinemas()

    override fun getCinema(id: Int): LiveData<Cinema> = cinemaDao.getCinema(id)
}