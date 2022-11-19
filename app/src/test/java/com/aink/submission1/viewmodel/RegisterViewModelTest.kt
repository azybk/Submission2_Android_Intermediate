package com.aink.submission1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.Result
import com.aink.submission1.getOrAwaitValue
import com.aink.submission1.repo.RegisterRepository
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registerRepository: RegisterRepository

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerRepository)
    }

    @Test
    fun `when Add User and Return Result Success`() {
        val expectedRegister = MutableLiveData<Result<String>>()
        expectedRegister.value = Result.Success("User created")

        `when`(registerRepository.registerUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        )).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.postRegisterUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        ).getOrAwaitValue()

        Mockito.verify(registerRepository).registerUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        )

        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Success)

    }

    @Test
    fun `when Failed Add User and Return Result Error`() {
        val expectedRegister = MutableLiveData<Result<String>>()
        expectedRegister.value = Result.Error("HTTP 400 Bad Request")

        `when`(registerRepository.registerUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        )).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.postRegisterUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        ).getOrAwaitValue()

        Mockito.verify(registerRepository).registerUser(
            RegisterUser("saya", "saya8976@gmail.com", "555555")
        )

        assertTrue(actualRegister is Result.Error)
    }

}