package com.aink.submission1.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.aink.submission1.data.LoginResult
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.ResponseLogin
import com.aink.submission1.data.Result
import com.aink.submission1.getOrAwaitValue
import com.aink.submission1.repo.LoginRepository
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginRepository: LoginRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `When Login User And Return Result Success`() {
        val expectedLogin = MutableLiveData<Result<ResponseLogin>>()
        expectedLogin.value = Result.Success(
            ResponseLogin(
                LoginResult(null, null, null),
                false, "success"
            )
        )

        `when`(loginRepository.loginUser(
            RegisterUser(null, "muraibatu@gmail.com", "111111")
        )).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.postLoginUser(
            RegisterUser(null, "muraibatu@gmail.com", "111111")
        ).getOrAwaitValue()

        Mockito.verify(loginRepository).loginUser(
            RegisterUser(null, "muraibatu@gmail.com", "111111")
        )

        assertTrue(actualLogin is Result.Success)

    }

    @Test
    fun `When Login User And Return Result Error`() {
        val expectedLogin = MutableLiveData<Result<ResponseLogin>>()
        expectedLogin.value = Result.Error("HTTP 401 Anauthorized")

        `when`(loginRepository.loginUser(
            RegisterUser(null, "muraibatu@gmail.com", "555555")
        )).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.postLoginUser(
            RegisterUser(null, "muraibatu@gmail.com", "555555")
        ).getOrAwaitValue()

        Mockito.verify(loginRepository).loginUser(
            RegisterUser(null, "muraibatu@gmail.com", "555555")
        )

        assertTrue(actualLogin is Result.Error)
    }

}