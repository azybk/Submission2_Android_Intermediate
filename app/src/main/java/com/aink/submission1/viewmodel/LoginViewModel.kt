package com.aink.submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.repo.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository = LoginRepository()): ViewModel() {

    fun postLoginUser(user: RegisterUser) = loginRepository.loginUser(user)

}