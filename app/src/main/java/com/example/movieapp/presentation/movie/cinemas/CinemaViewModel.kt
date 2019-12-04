package com.example.movieapp.presentation.movie.cinemas

import androidx.lifecycle.LiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.repository.CinemaRepositoryImpl
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.CinemaDao
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.repository.CinemaRepository

class CinemaViewModel(
    private val cinemaDao: CinemaDao
): BaseViewModel() {


    private val repository: CinemaRepository

    var liveData : LiveData<List<Cinema>>

    init {
        repository = CinemaRepositoryImpl(cinemaDao)
        liveData = repository.getAllCinemas()
    }

    override fun handleError(e: Throwable) {
        if (e is NoConnectionException) {
            //ToDo
        }
    }
}