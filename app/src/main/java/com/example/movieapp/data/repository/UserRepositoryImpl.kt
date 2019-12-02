package com.example.movieapp.data.repository

import com.example.movieapp.data.models.AccountData
import com.example.movieapp.data.network.ApiClient
import com.example.movieapp.data.network.MovieApi
import com.example.movieapp.domain.repository.UserRepository
import com.google.gson.JsonObject
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    private var requestToken: String? = null

    override suspend fun createToken(): Response<JsonObject> {
        val requestTokenResponse = ApiClient.apiClient.createRequestToken().await()
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
        val loginResponse = ApiClient.apiClient.login(body).await()
        return loginResponse.body()?.getAsJsonPrimitive("success")?.asBoolean ?: false
    }

    override suspend fun createSession(): Response<JsonObject> {
        val body = JsonObject().apply {
            addProperty("request_token", requestToken)
        }
        return ApiClient.apiClient.createSession(body).await()
    }

    //Account
    override suspend fun getAccountDetails(sessionId: String): AccountData? =
        ApiClient.apiClient.getAccountId(sessionId).await().body()
}