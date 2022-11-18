package com.aink.submission1.viewmodel

import androidx.lifecycle.ViewModel
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.repo.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository = RegisterRepository()): ViewModel() {

    fun postRegisterUser(user: RegisterUser) = registerRepository.registerUser(user)

}