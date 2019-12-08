package com.example.movieapp.presentation.movie.cinemas.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.repository.CinemaRepositoryImpl
import com.example.movieapp.data.room.Cinema
import com.example.movieapp.data.room.CinemaRoomDatabase
import com.example.movieapp.repository.CinemaRepository
import kotlinx.coroutines.launch

class CinemaDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val repository: CinemaRepository

    lateinit var liveData: LiveData<Cinema>

    init {
        val cinemaDao = CinemaRoomDatabase.getDatabase(application, viewModelScope).cinemaDao()
        repository = CinemaRepositoryImpl(cinemaDao)
    }

    fun getCinema(id: Int) {
        viewModelScope.launch {
            val cinema = repository.getCinema(id)
            cinema.let { cinema ->
                liveData = cinema
            }
        }
    }
}