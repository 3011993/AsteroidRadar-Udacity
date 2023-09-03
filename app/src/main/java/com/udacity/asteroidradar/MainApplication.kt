package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.worker.DataWorker
import com.udacity.asteroidradar.worker.WORKER_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayInit()
    }

    private fun delayInit() = applicationScope.launch {
        setupRecurringWorker()
    }

    private fun setupRecurringWorker() {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.METERED)
            .setRequiresCharging(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<DataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraint)
            .build()


        WorkManager.getInstance().enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}