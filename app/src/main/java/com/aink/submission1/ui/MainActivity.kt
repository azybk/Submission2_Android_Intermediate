package com.aink.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aink.submission1.data.LoginResult
import com.aink.submission1.data.RegisterUser
import com.aink.submission1.data.Result
import com.aink.submission1.databinding.ActivityMainBinding
import com.aink.submission1.utils.UserPreference
import com.aink.submission1.utils.util
import com.aink.submission1.viewmodel.LoginViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var email: EditText
    private lateinit var password: EditText
    private val vieModel: LoginViewModel by viewModels()

    private lateinit var mUserPrefernces: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = binding.edLoginEmail
        password = binding.edLoginPassword

        mUserPrefernces = UserPreference(this)

        if (mUserPrefernces.getUser() != "") {
            val intent = Intent(this@MainActivity, StoriesActivity::class.java)
            startActivity(intent)
        }
        else {
            showLoginRegister()
        }

    }


    private fun showLoginRegister() {
        binding.btnLogin.setOnClickListener {
            if (util.checkEmail(email.text.trim().toString()).isNotEmpty()) {
                email.error = util.checkEmail(email.text.trim().toString())
            }
            else if (util.checkPasswordLength(password.text.toString()).isNotEmpty()) {
                password.error = util.checkPasswordLength(password.text.toString())
            }
            else {
                val loginUser = RegisterUser(
                    null,
                    email.text.trim().toString(),
                    password.text.toString()
                )

                vieModel.postLoginUser(loginUser).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Success -> {
                                val session = LoginResult(
                                    result.data.loginResult?.userId,
                                    result.data.loginResult?.name,
                                    result.data.loginResult?.token
                                )
                                mUserPrefernces.saveUser(session)

                                val intent = Intent(this@MainActivity, StoriesActivity::class.java)
                                startActivity(intent)

                                Log.d("MainActivity", "showLoginRegister: ${result.data.message}")
                            }

                            is Result.Error -> {
                                Toast.makeText(this@MainActivity, result.error, Toast.LENGTH_SHORT).show()
                                Log.d("MainActivity", "showLoginRegister: ${result.error}")
                            }
                        }
                    }
                }

            }
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}