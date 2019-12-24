package com.example.movieapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    private val job = SupervisorJob()

    private val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val uiScope: CoroutineScope = CoroutineScope(coroutineContext)

    fun login(username: String, password: String) {
        uiScope.launch {
            _liveData.value = State.ShowLoading
            val token = withContext(Dispatchers.IO) {
                userRepository.createToken()
                userRepository.login(username, password)
            }
            val sessionId: String = withContext(Dispatchers.IO) {
                userRepository.createSession().body()!!.getAsJsonPrimitive("session_id").asString
            }
            _liveData.value = State.HideLoading
            _liveData.postValue(State.ApiResult(token, sessionId))
        }
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class ApiResult(val success: Boolean, val session_id: String): State()
    }
}



