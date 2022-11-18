package com.aink.submission1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aink.submission1.config.ApiConfig
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.ResponseLogin
import com.aink.submission1.data.Result

class LoginRepository {

    fun loginUser(user: RegisterUser): LiveData<Result<ResponseLogin>> = liveData {
        emit(Result.Loading)

        try {
            val response = ApiConfig().getApiService().loginUser(user)

            emit(Result.Success(response))

        } catch (e: Exception) {
            Log.d("loginUser", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }

    }

}