package com.example.movieapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cinema_table")
data class Cinema (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = null,
    val address: String? = null,
    val parking: String? = null,
    val entry: String? = null,
    val phoneNumber: String? = null,
    val buffet: String?  = null,
    val poster: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)