package com.aink.submission1.utils

import android.content.Context
import com.aink.submission1.R
import com.aink.submission1.data.LoginResult

class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(R.string.pref_user.toString(), Context.MODE_PRIVATE)

    fun saveUser(user: LoginResult) {
        val editor = preferences.edit()
        editor.putString(R.string.user_id.toString(), user.userId)
        editor.putString(R.string.nama.toString(), user.name)
        editor.putString(R.string.token.toString(), user.token)
        editor.apply()
    }

    fun getUser(): String? {
        val token = preferences.getString(R.string.token.toString(), "")

        return token?.toString()
    }

    fun hapusUser(context: Context) {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

}