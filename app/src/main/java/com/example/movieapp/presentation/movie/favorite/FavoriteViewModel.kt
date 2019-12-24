package com.example.movieapp.presentation.movie.favorite

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

class FavoriteViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel(){

    private val compositeDisposable = CompositeDisposable()
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    override fun handleError(e: Throwable) {
        if (e is NoConnectionException) {
            _liveData.value = State.IntError(e.messageInt)
        } else {
            _liveData.value = State.Error(e.localizedMessage)
        }
    }


    fun loadFavMovies(sessionId: String?, page: Int = 1) {
        if (page == 1) {
            _liveData.value = State.ShowLoading
        }
        sessionId?.let {
            compositeDisposable.addAll(
                movieRepository.getFavoriteMovies(it, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        val list = response.results ?: emptyList()
                        val totalPages = response.totalPages ?: 0
                        _liveData.postValue(
                            State.Result(
                                totalPages = totalPages,
                                list = list
                            )
                        )
                    }, { t ->
                        _liveData.value = State.Error(t.message)
                    })
            )
        }
        _liveData.value = State.HideLoading
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPages: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}

