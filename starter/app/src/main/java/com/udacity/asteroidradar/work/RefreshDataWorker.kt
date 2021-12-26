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
    
    override suspend fun doWork(): Result { // Init the repo
        val database: AppDatabase = getDatabase(context = applicationContext)
        val repository = AsteroidRepository(database)
        
        // Try to get refresh the asteroid data and cache in the db.
        return try {
            //Delete all data.
            repository.deleteAsteroidData()
            
            //Fetch and cache new data
            repository.refreshAsteroidData()
            
            Result.success() //If it good
        } catch (e: HttpException) {
            Result.retry() //try later. If it a Http exception.
        }
    }
}