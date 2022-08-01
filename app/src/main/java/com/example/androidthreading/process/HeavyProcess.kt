package com.example.androidthreading.process

class HeavyProcess
{
    companion object {
        fun run(weight: Int): Int
        {
            var result = 0
            for(i in 1..weight) for(j in 1..weight) result += 1
            return result
        }
    }
}