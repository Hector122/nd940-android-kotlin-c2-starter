package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AppDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    // Identify the worker
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    
    override suspend fun doWork(): Result {
        //init the repo
        val database: AppDatabase = getDatabase(context = applicationContext)
        val repository: AsteroidRepository = AsteroidRepository(database)
        
        // try to get refresh the asteroid data.
        return try {
            repository.refreshAsteroidData()
            Result.success() //If it good
        } catch (e: HttpException) {
            Result.retry() //try later. If it a Http exception.
        }
    }
    
}