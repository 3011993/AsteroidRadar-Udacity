package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

const val WORKER_NAME = "RefreshDataWork"

class DataWorker(appContext : Context, parameters: WorkerParameters) : CoroutineWorker(appContext,parameters) {


    override suspend fun doWork(): Result {

        val database = getDatabase(applicationContext)
        val asteroidsRepository = AsteroidsRepository(database)
        return try {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshImages()
            Result.success()
        } catch (e : HttpException){
            Result.retry()
        }
    }



}