package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.model.Asteroid


@Dao
interface AsteroidDao {
    
    @Query("SELECT * FROM databaseasteroid WHERE close_approach_date >= DATE() ORDER BY close_approach_date ASC")
    fun getAllAsteroid(): LiveData<List<DataBaseAsteroid>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Upsert inset or update
    fun insertAll(vararg dataBaseAsteroid: DataBaseAsteroid)
    
    @Query("DELETE FROM databaseasteroid WHERE close_approach_date < DATE()")
    fun deleteAsteroid()
}

@Database(entities = [DataBaseAsteroid::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: AsteroidDao
}


private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "asteroid")
                .build()
        }
    }
    return INSTANCE
}