package com.aink.submission1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aink.submission1.config.ApiConfig
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.Result

class RegisterRepository {

    fun registerUser(user: RegisterUser): LiveData<Result<String>> = liveData {
        emit(Result.Loading)

        try {
            val response = ApiConfig().getApiService().registerUser(user)

            emit(Result.Success(response.message))

        } catch (e: Exception) {
            Log.d("RegisterRepository", "registerUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

}