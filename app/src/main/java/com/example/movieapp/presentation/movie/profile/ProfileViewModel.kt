package com.example.movieapp.presentation.movie.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.base.BaseViewModel
import com.example.movieapp.data.models.AccountData
import com.example.movieapp.data.repository.UserRepositoryImpl
import com.example.movieapp.domain.repository.UserRepository
import com.example.movieapp.exceptions.NoConnectionException
import com.example.movieapp.extensions.launchSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileViewModel : BaseViewModel() {


    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private val userRepository: UserRepository = UserRepositoryImpl()


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
        _liveData.value =
            State.ShowLoading
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                userRepository.getAccountDetails(sessionId)
            }
            _liveData.postValue(
                State.Result(result)
            )
        }
        _liveData.value =
            State.HideLoading

    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val account: AccountData?): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }

}


