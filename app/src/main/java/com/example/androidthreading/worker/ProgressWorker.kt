package com.example.androidthreading.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ProgressWorker(context: Context, parameters: WorkerParameters):
    CoroutineWorker(context, parameters)
{
        companion object {
            const val Progress = "Progress"
            private const val delayDuration = 1000L
        }

    override suspend fun doWork(): Result {
        val firstUpdate = workDataOf(Progress to 0)
        val startUpdate = workDataOf(Progress to 25)
        val middleUpdate = workDataOf(Progress to 50)
        val endUpdate = workDataOf(Progress to 75)
        val lastUpdate = workDataOf(Progress to 100)
        setProgress(firstUpdate)
        delay(delayDuration)
        setProgress(startUpdate)
        delay(delayDuration)
        setProgress(middleUpdate)
        delay(delayDuration)
        setProgress(endUpdate)
        delay(delayDuration)
        setProgress(lastUpdate)
        return Result.success()
    }
}