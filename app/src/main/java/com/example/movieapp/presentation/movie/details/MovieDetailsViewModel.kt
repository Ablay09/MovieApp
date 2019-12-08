package com.example.movieapp.presentation.movie.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    fun getMovie(movieId: Int) {
        uiScope.launchSafe(::handleError) {
            _liveData.value =
                State.ShowLoading
            withContext(Dispatchers.IO) {
                val movie = movieRepository.getMovieById(movieId)
                movie?.let { movieData ->
                    _liveData.postValue (
                        State.Result(movieData)
                    )
                }
            }
            _liveData.value =
                State.HideLoading
        }
    }

    fun setFavorite(accountId: Int, movieId: Int, sessionId: String, favorite: Boolean) {
        uiScope.launchSafe(::handleError) {
            withContext(Dispatchers.Default) {
                val resultCode: Int? = movieRepository.rateMovie(movieId, accountId, sessionId, favorite)
                resultCode?.let { code ->
                    _liveData.postValue(
                        State.FavoriteMovie(
                            code
                        )
                    )
                }
            }
        }
    }

    override fun handleError(e: Throwable) {
        _liveData.value =
            State.HideLoading
        if (e is NoConnectionException) {
            _liveData.value =
                State.IntError(
                    e.messageInt
                )
        } else {
            _liveData.value =
                State.Error(
                    e.localizedMessage
                )
        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val movie: MovieData): State()
        data class FavoriteMovie(val resultCode: Int): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}