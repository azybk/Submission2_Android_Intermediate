package com.aink.submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.aink.submission1.repo.AddStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val addStoryRepository: AddStoryRepository = AddStoryRepository()): ViewModel() {

    fun postAddStory(description: RequestBody,photo: MultipartBody.Part,authoriztion: String) =
        addStoryRepository.addStory(description, photo, authoriztion)

}