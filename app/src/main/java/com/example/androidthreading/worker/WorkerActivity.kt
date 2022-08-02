package com.example.androidthreading.worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import com.example.androidthreading.R
import com.example.androidthreading.databinding.ActivityWorkerBinding
import java.util.*
import java.util.concurrent.TimeUnit

class WorkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerBinding
    private var periodicWorkId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.doWork.setOnClickListener { simpleWork() }
        binding.doPeriodicWork.setOnClickListener { periodicWork() }
        binding.doUniqueWork.setOnClickListener { uniqueWork() }
        binding.cancelPeriodicWork.setOnClickListener { cancelPeriodicWork() }
        binding.doProgressWork.setOnClickListener { progressWork() }
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
        periodicWorkId = periodicWorkRequest.id
    }

    private fun enqueue(request: WorkRequest)
    {
        WorkManager
            .getInstance(this)
            .enqueue(request)

        WorkManager
            .getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observe(this) {
                if(it.state == WorkInfo.State.SUCCEEDED)
                {
                    Toast.makeText(this, "Work completed", Toast.LENGTH_LONG).show()
                }
            }
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

    private fun cancelPeriodicWork()
    {
        if(periodicWorkId !== null)
        {
            WorkManager.getInstance(this).cancelWorkById(periodicWorkId!!)
        }
    }

    private fun progressWork()
    {
        val request = OneTimeWorkRequestBuilder<ProgressWorker>()
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniqueWork("Progress", ExistingWorkPolicy.KEEP, request)

        WorkManager
            .getInstance(this)
            .getWorkInfoByIdLiveData(request.id)
            .observe(this) { workInfo: WorkInfo? ->
                if(workInfo != null)
                {
                    val progress = workInfo.progress
                    val value = progress.getInt(ProgressWorker.Progress, 0)
                    binding.doProgressWork.text = getString(R.string.progress_is, value)
                    if(workInfo.state == WorkInfo.State.SUCCEEDED)
                    {
                        binding.doProgressWork.text = getString(R.string.do_progress_work)
                    }
                }
            }
    }

}