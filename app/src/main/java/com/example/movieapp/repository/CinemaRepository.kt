package com.example.movieapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.data.room.Cinema

interface CinemaRepository {

    fun getAllCinemas(): LiveData<List<Cinema>>

    fun getCinema(id: Int): LiveData<Cinema>
}