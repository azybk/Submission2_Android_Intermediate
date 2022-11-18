package com.aink.submission1

import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.data.UploadResponse

object DataDummy {

    fun generateDummyMapList(): List<ListStoryItem> {
        val listStoryItem = ArrayList<ListStoryItem>()

        for (i in 1..30) {
            val mapsList = ListStoryItem(
                "photo-$i.jpg",
                "nama: $i",
                "description-$i",
                "id-$i",
                i.toDouble(),
                i.toDouble()
            )
            listStoryItem.add(mapsList)
        }
        return listStoryItem
    }

    const val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVFLRVZyaURBLWlxZ21fazIiLCJpYXQiOjE2Njg1NjM1NzZ9.xk3Zx3dDM8ZKs_s8afMoa96fRwn-muDyDYPtEtxTOkg"

    fun addDummyStory(): UploadResponse {
        return UploadResponse(false, "Add Story Successfully")
    }

}