package com.example.androidthreading.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.example.androidthreading.databinding.ActivityWorkerBinding
import java.util.concurrent.TimeUnit

class WorkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.doWork.setOnClickListener { simpleWork() }
        binding.doPeriodicWork.setOnClickListener { periodicWork() }
        binding.doUniqueWork.setOnClickListener { uniqueWork() }
    }

    private fun simpleWork()
    {
        val customWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<CustomWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
        enqueue(customWorkRequest)
    }

    private fun periodicWork()
    {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<CustomWorker>(20, TimeUnit.MINUTES)
                .build()
        enqueue(periodicWorkRequest)
    }

    private fun enqueue(request: WorkRequest)
    {
        WorkManager
            .getInstance(this)
            .enqueue(request)
    }

    private fun uniqueWork()
    {
        val customWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<CustomWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

        WorkManager
            .getInstance(this)
            .enqueueUniqueWork("unique", ExistingWorkPolicy.KEEP, customWorkRequest)
    }


}