package com.example.movieapp.presentation.movie.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.AccountData
import com.example.movieapp.domain.repository.UserRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.extensions.launchSafe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val userRepository: UserRepository
): BaseViewModel() {

    private val compositeDisposable = CompositeDisposable()
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


    fun getAccountDetails(sessionId: String) {
        compositeDisposable.add(
            userRepository.getAccountDetails(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    _liveData.value =
                        State.HideLoading
                    _liveData.postValue(
                        State.Result(result)
                    )
                }, {
                    _liveData.value =
                        State.Error(it.message)
                })
        )
        _liveData.value =
            State.ShowLoading

    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val account: AccountData?): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }

}


