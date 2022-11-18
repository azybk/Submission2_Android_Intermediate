package com.aink.submission1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aink.submission1.config.ApiConfig
import com.aink.submission1.data.Result
import com.aink.submission1.data.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryRepository {

    fun addStory(description: RequestBody, photo: MultipartBody.Part, authorization: String): LiveData<Result<UploadResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = ApiConfig().getApiService().addStory(description, photo, "Bearer $authorization")
            val uploadResponse = response

            emit(Result.Success(uploadResponse))

        } catch (e: Exception) {
            Log.d("AddStoryRepository", "addStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

    }

}
