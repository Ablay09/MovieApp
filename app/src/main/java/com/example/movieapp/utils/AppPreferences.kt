package com.example.movieapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private val APP_SETTINGS = "APP_SETTINGS"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getAccountId(context: Context): Int? =
        getSharedPreferences(context).getInt(AppConstants.ACCOUNT_ID, 0)

    fun setAccountId(context: Context, newValue: Int?) {
        val editor = getSharedPreferences(context).edit()
        if (newValue != null) {
            editor.putInt(AppConstants.ACCOUNT_ID, newValue)
        }
        editor.commit()
    }

    fun getSessionId(context: Context): String? =
        getSharedPreferences(context).getString(AppConstants.SESSION_ID, null)

    fun setSessionId(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        if (newValue != null) {
            editor.putString(AppConstants.SESSION_ID, newValue)
        }
        editor.commit()
    }
}