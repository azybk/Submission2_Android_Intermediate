package com.aink.submission1.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.aink.submission1.config.ApiConfig
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.data.Result
import com.aink.submission1.data.StoriesPagingSource


class ListStoryRepository {

    fun listStory(authorization: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoriesPagingSource(ApiConfig().getApiService(), "Bearer $authorization")
            }
        ).liveData
    }

    fun listStoryMap(authorization: String): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)

        try {
            val response = ApiConfig().getApiService().listMapStories("Bearer $authorization", null, null, 1)
            val listMapsItem = response.listStory

            val responseMapsList = listMapsItem.map { maps ->
                ListStoryItem(
                    photoUrl = "photo",
                    name = maps.name,
                    description = "deskripsi",
                    id = "id",
                    lat = maps.lat,
                    lon = maps.lon
                )
            }
            emit(Result.Success(responseMapsList))

        } catch (e: Exception) {
            Log.d("ListStoryRepository", "listStoryMap: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

    }
}
