package com.aink.submission1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.repo.ListStoryRepository


class ListStoryViewModel(private val listStoryRepository: ListStoryRepository = ListStoryRepository()): ViewModel() {

    fun listStory(token: String): LiveData<PagingData<ListStoryItem>> =
        listStoryRepository.listStory(token).cachedIn(viewModelScope)

}