package com.aink.submission1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aink.submission1.DataDummy
import com.aink.submission1.data.Result
import com.aink.submission1.data.UploadResponse
import com.aink.submission1.getOrAwaitValue
import com.aink.submission1.repo.AddStoryRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File


@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addStoryRepository: AddStoryRepository

    private lateinit var addStoryViewModel: AddStoryViewModel
    private var dummyAddStory = DataDummy.addDummyStory()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(addStoryRepository)
    }

    @Test
    fun `When Add Story And Return Result Success`() {
        val imageDescription = "Deskripsi Gambar"

        var fileImage = File("photo", "photoDummy.jpg")

        val description = imageDescription.toRequestBody("text/plain".toMediaType())
        val requestImageFile = fileImage.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", fileImage.name, requestImageFile)

        val expectedAddStory = MutableLiveData<Result<UploadResponse>>()
        expectedAddStory.value = Result.Success(dummyAddStory)

        `when`(addStoryRepository.addStory(description, imageMultipart, DataDummy.token)).thenReturn(expectedAddStory)

        val actualAddStory = addStoryViewModel.postAddStory(description, imageMultipart, DataDummy.token).getOrAwaitValue()

        Mockito.verify(addStoryRepository).addStory(description, imageMultipart, DataDummy.token)

        assertTrue(actualAddStory is Result.Success)

    }

}
