package com.example.androidthreading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.androidthreading.databinding.ActivityLoginBinding
import com.example.androidthreading.process.HeavyProcess
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private  val mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.login.setOnClickListener { login() }

    }

    private fun login()
    {
        executorService.execute {
            val result = syncLogin()
            mainThreadHandler.post { loginCallback(result) }
        }
    }

    private fun loginCallback(result: Result<Boolean>)
    {
        when (result)
        {
            is Result.Success -> {
                if(result.data) binding.loginResult.text = getString(R.string.login_was, "Successful")
                else binding.loginResult.text = getString(R.string.login_was, "Unsuccessful")
            }
            else -> binding.loginResult.text = getString(R.string.login_was, "Error")
        }
    }

    private fun syncLogin(): Result<Boolean>
    {
        return try {
            val weight = binding.username.text.length*binding.password.text.length*1000
            binding.login.text = getString(R.string.loading)
            HeavyProcess.run(weight);
            binding.login.text = getString(R.string.login)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    sealed class Result<out R> {
        data class Success<out T>(val data: T): Result<T>()
        data class Error(val exception: Exception): Result<Nothing>()
    }
}