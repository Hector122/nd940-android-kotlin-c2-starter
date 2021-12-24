package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    
    // best practice.
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    
    override fun onCreate() {
        super.onCreate()
        
        scheduleRecurringWork()
    }
    
    private fun scheduleRecurringWork() {
        applicationScope.launch {
            
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()
            
            //Periodic request 1 a day
            val repeatingRequest =
                PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).setConstraints(
                        constraints)
                    .build()
            
            // Execute regulary
            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        repeatingRequest)
        }
        
    }
}