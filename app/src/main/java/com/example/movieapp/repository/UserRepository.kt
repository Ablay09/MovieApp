package com.example.movieapp.domain.repository

import com.example.movieapp.data.models.AccountData
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response

interface UserRepository {

//    fun login(username: String, password: String): Single<Boolean>
//
//    fun createSession(): Single<String>

    suspend fun createToken(): Response<JsonObject>

    suspend fun createSession(): Response<JsonObject>

    suspend fun login (username: String, password: String) : Boolean

    fun getAccountDetails(sessionId: String): Observable<AccountData>
}