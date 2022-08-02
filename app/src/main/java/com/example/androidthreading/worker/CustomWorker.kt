package com.example.androidthreading.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidthreading.process.HeavyProcess

class CustomWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams)
{
    private val tag = "CUSTOM_WORKER"
    override fun doWork(): Result {
        val result = HeavyProcess.run(100000)
        Log.d(tag, "Custom worker finished with result: $result")
        return Result.success()
    }
}