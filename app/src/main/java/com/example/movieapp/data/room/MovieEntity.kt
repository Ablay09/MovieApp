package com.example.movieapp.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies_table")
data class MovieEntity (
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    var movieId: Int = 0
)