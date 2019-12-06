package com.example.movieapp.presentation.movie.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.MovieData
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.exceptions.NoConnectionException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieListViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    val liveData: LiveData<State>
        get() = _liveData

    private val _liveData = MutableLiveData<State>()
    private val compositeDisposable = CompositeDisposable()


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
        if (page == 1) {
            _liveData.value =
                State.ShowLoading
        }
        compositeDisposable.add(
            movieRepository.getPopularMovies(page)
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
                }, { t: Throwable? ->
                    _liveData.postValue(
                        State.Error(t?.message)
                    )
                })
        )
        _liveData.value =
            State.HideLoading
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPages: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }

}

