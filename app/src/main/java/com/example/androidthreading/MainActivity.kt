package com.example.androidthreading

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.androidthreading.databinding.ActivityMainBinding
import com.example.androidthreading.process.HeavyProcess
import com.example.androidthreading.worker.WorkerActivity
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val broadcastUrl = "com.example.androidthreading.MY_NOTIFICATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindListeners()
        registerReceiver()

    }

    private fun bindListeners()
    {
        binding.weightText.addTextChangedListener(object: TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(text: CharSequence?, start: Int, count: Int, arter: Int) {
                binding.runButton.isEnabled = text?.isNotEmpty() ?: false
            }

            override fun afterTextChanged(p0: Editable?) { }

        })

        binding.runButton.setOnClickListener { runHeavyCalculation() }

        binding.toLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.toWorkManager.setOnClickListener {
            startActivity(Intent(this, WorkerActivity::class.java))
        }

        binding.generateBroadcast.setOnClickListener { sendBroadcastMessage() }
    }

    private fun runHeavyCalculation()
    {

        val weight = binding.weightText.text.toString().toInt()
        if(binding.onMain.isChecked)
        {
            binding.result.text = getString(R.string.result, HeavyProcess.run(weight))
        }
        else
        {
            Thread {
                val result = HeavyProcess.run(weight)
                runOnUiThread {  binding.result.text = getString(R.string.result, result) }
            }.start()
        }
    }

    private fun registerReceiver()
    {
        val br = MyBroadcastReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(broadcastUrl)
        }
        registerReceiver(br, filter)
    }

    private fun sendBroadcastMessage()
    {
        Intent().also {
            it.action = broadcastUrl
            it.putExtra("data", "Nothing to see here, move along")
            sendBroadcast(it)
        }
    }
}