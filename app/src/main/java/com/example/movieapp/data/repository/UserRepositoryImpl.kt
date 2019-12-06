package com.example.movieapp.data.repository

import com.example.movieapp.data.models.AccountData
import com.example.movieapp.data.network.MovieApi
import com.example.movieapp.domain.repository.UserRepository
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class UserRepositoryImpl(
    private val movieApi: MovieApi
): UserRepository {
    private var requestToken: String? = null

//    override fun login(username: String, password: String): Single<Boolean> {
//        return movieApi.createRequestToken().map{response ->
//            response.body()?.getAsJsonPrimitive("request_token")?.asString
//        }.flatMap {token ->
//            val body = JsonObject().apply {
//                addProperty("username", username)
//                addProperty("password", password)
//                addProperty("request_token", token)
//            }
//            movieApi.login(body)
//        }.map{ response ->
//            response.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
//        }
//    }
//
//
//    override fun createSession(): Single<String> {
//        return movieApi.createRequestToken().map{response ->
//            response.body()?.getAsJsonPrimitive("request_token")?.asString
//        }.flatMap { token ->
//            val body = JsonObject().apply {
//                addProperty("request_token", token)
//            }
//            movieApi.createSession(body)
//        }.map { response ->
//            response.body()?.getAsJsonPrimitive("session_id")?.asString ?: ""
//        }
//    }


    override suspend fun createToken(): Response<JsonObject> {
        val requestTokenResponse = movieApi.createRequestToken().await()
        requestToken = requestTokenResponse
            .body()
            ?.getAsJsonPrimitive("request_token")
            ?.asString
        return requestTokenResponse
    }

    override suspend fun login(username: String, password: String) : Boolean {
        val body = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("request_token", requestToken)
        }
        val loginResponse = movieApi.login(body).await()
        return loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
    }


    override suspend fun createSession(): Response<JsonObject> {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return movieApi.createSession(body).await()
    }

    override fun getAccountDetails(sessionId: String): Observable<AccountData> =
        movieApi.getAccountId(sessionId)
}