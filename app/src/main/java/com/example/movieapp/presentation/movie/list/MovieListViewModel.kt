package com.example.movieapp.presentation.movie.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieListViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    override fun handleError(e: Throwable) {
        _liveData.value =
            State.HideLoading
        if(e is NoConnectionException) {
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

    init {
        loadMovies()
    }

    fun loadMovies(page: Int = 1) {
        uiScope.launchSafe(::handleError) {
            if (page == 1) {
                _liveData.value =
                    State.ShowLoading
            }
            val result = withContext(Dispatchers.IO) {
                val response = movieRepository.getPopularMovies(page)
                val list = response?.results ?: emptyList()
                val totalPages = response?.totalPages ?: 0
                Pair(totalPages, list)
            }
            _liveData.postValue(
                State.Result(
                    totalPage = result.first,
                    list = result.second
                )
            )
            _liveData.value =
                State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPage: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}