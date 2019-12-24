package com.example.movieapp.presentation.movie.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.data.repository.CinemaRepositoryImpl
import com.example.movieapp.data.room.CinemaDao
import com.example.movieapp.data.room.MovieEntity
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.repository.CinemaRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val cinemaDao: CinemaDao
): ViewModel() {

    private val parentJob = SupervisorJob()

    private val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    protected val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val compositeDisposable = CompositeDisposable()
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private val repository: CinemaRepository

    init {
        repository = CinemaRepositoryImpl(cinemaDao)
    }

    fun addToFavorites(movieId: Int) {
        uiScope.launch {
            repository.insertMovie(movieEntity = MovieEntity(movieId=movieId))
        }
    }

    fun deleteFromFavorites(movieId: Int) {
        uiScope.launch {
            repository.deleteFromFavorites(movieId)
            _liveData.postValue (
                State.IsFavorite(false))
        }
    }

    fun checkIfFavorite(movieId: Int) {
        uiScope.launch {
            if (repository.checkIfFavorite(movieId)) {
                _liveData.postValue (
                    State.IsFavorite(true))
            } else {
                Log.d("FavoriteMovie:", "Lol, add some movies first")
            }
        }
    }

    fun getMovie(movieId: Int) {
        compositeDisposable.add(
            movieRepository.getMovieById(movieId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie ->
                    _liveData.value = State.HideLoading
                    _liveData.postValue (
                        State.Result(movie)
                    )
                }, { throwable ->
                    _liveData.value =
                        State.Error(throwable.message)
                })
        )
        _liveData.value =
            State.ShowLoading
    }

    fun setFavorite(accountId: Int, movieId: Int, sessionId: String, favorite: Boolean) {
        compositeDisposable.add(
            movieRepository.favMovie(movieId, accountId, sessionId, favorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ code ->
                    _liveData.postValue(
                        State.FavoriteMovie(code)
                    )
                }, {
                    _liveData.value =
                        State.Error(it.message)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class IsFavorite(val favorite: Boolean): State()
        data class Result(val movie: MovieData): State()
        data class FavoriteMovie(val resultCode: Int): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}