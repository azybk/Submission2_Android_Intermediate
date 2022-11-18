package com.aink.submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.aink.submission1.repo.ListStoryRepository


class ListMapViewModel(private val listMapRepository: ListStoryRepository = ListStoryRepository()): ViewModel() {

    fun getMapStory(authorization: String) = listMapRepository.listStoryMap(authorization)

}

