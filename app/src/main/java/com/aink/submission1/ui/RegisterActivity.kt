package com.aink.submission1.ui

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.Result
import com.aink.submission1.databinding.ActivityRegisterBinding
import com.aink.submission1.utils.util
import com.aink.submission1.viewmodel.RegisterViewModel


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var nama: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nama = binding.edRegisterName
        email = binding.edRegisterEmail
        password = binding.edRegisterPassword

        binding.btnRegisterUser.setOnClickListener {
            if (util.checkNama(nama.text.trim().toString()).isNotEmpty()) {
                nama.error = util.checkNama(nama.text.trim().toString())
            }
            else if (util.checkEmail(email.text.trim().toString()).isNotEmpty()) {
                email.error = util.checkEmail(email.text.trim().toString())
            }
            else if (util.checkPasswordLength(password.text.toString()).isNotEmpty()) {
                password.error = util.checkPasswordLength(password.text.toString())
            }
            else {
                val user = RegisterUser(
                    nama.text.trim().toString(),
                    email.text.trim().toString(),
                    password.text.toString()
                )

                registerViewModel.postRegisterUser(user).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Success -> {
                                Toast.makeText(this@RegisterActivity, result.data, Toast.LENGTH_SHORT).show()
                                Log.d("RegisterActivity", result.data)
                            }
                            is Result.Error -> {
                                Toast.makeText(this@RegisterActivity, result.error, Toast.LENGTH_SHORT).show()
                                Log.d("RegisterActivity", result.error)
                            }
                        }
                    }
                }
            }
        }

    }

}