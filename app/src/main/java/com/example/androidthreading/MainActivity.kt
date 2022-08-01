package com.example.androidthreading

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.androidthreading.databinding.ActivityMainBinding
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindListeners()


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

        binding.runButton.setOnClickListener {
            val weight = binding.weightText.text.toString().toInt()
            if(binding.onMain.isChecked)
            {
                binding.result.text = getString(R.string.result, heavyProcess(weight))
            }
            else
            {
                Thread {
                    val result = heavyProcess(weight)
                    runOnUiThread {  binding.result.text = getString(R.string.result, result) }
                }.start()
            }
        }

        binding.toLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun heavyProcess(weight: Int): Int
    {
        var result = 0
        for(i in 1..weight) for(j in 1..weight) result += 1
        return result
    }
}