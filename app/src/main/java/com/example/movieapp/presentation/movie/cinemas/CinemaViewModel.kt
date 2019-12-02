package com.example.movieapp.presentation.movie.cinemas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.repository.CinemaRepositoryImpl
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.CinemaRoomDatabase
import com.example.movieapp.domain.repository.CinemaRepository

class CinemaViewModel(application: Application): AndroidViewModel(application) {


    private val repository: CinemaRepository

    var liveData : LiveData<List<Cinema>>

    init {
        val cinemaDao = CinemaRoomDatabase.getDatabase(application, viewModelScope).cinemaDao()
        repository = CinemaRepositoryImpl(cinemaDao)
        liveData = repository.getAllCinemas()
    }
}