package com.example.movieapp.presentation.movie.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.extensions.launchSafe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

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
            movieRepository.rateMovie(movieId, accountId, sessionId, favorite)
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