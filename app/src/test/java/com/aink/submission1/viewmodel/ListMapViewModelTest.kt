package com.aink.submission1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aink.submission1.DataDummy
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.data.Result
import com.aink.submission1.getOrAwaitValue
import com.aink.submission1.repo.ListStoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ListMapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var listStoryRepository: ListStoryRepository

    private lateinit var listMapViewModel: ListMapViewModel
    private val dummyMaps = DataDummy.generateDummyMapList()

    @Before
    fun setUp() {
        listMapViewModel = ListMapViewModel(listStoryRepository)
    }

    @Test
    fun `when Get Map Should Not Null and Return Result Success`() {
        val expectedMaps = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedMaps.value = Result.Success(dummyMaps)

        `when`(listStoryRepository.listStoryMap(DataDummy.token)).thenReturn(expectedMaps)

        val actualMaps = listMapViewModel.getMapStory(DataDummy.token).getOrAwaitValue()

        Mockito.verify(listStoryRepository).listStoryMap(DataDummy.token)

        assertNotNull(actualMaps)
        assertTrue(actualMaps is Result.Success)

    }

    @Test
    fun `When Data Maps is Not Appear and Return Result Error`() {
        val errorMaps = MutableLiveData<Result<List<ListStoryItem>>>()
        errorMaps.value = Result.Error("Error")

        `when`(listStoryRepository.listStoryMap(DataDummy.token)).thenReturn(errorMaps)

        val actualMaps = listMapViewModel.getMapStory(DataDummy.token).getOrAwaitValue()

        Mockito.verify(listStoryRepository).listStoryMap(DataDummy.token)

        assertNotNull(actualMaps)
        assertTrue(actualMaps is Result.Error)
    }
}