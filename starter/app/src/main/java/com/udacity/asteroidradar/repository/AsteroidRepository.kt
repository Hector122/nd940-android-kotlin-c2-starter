package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AppDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.network.NetworkAsteroidContainer
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * Repo for fetching asteroid data info
 */
class AsteroidRepository(private val database: AppDatabase) {
    
    // Get a livedata objet with the db info and transform to domain objet.
    val asteroidsData: LiveData<List<Asteroid>> =
        Transformations.map(database.dao.getAllAsteroid()) {
            it.asDomainModel()
        }
    
    suspend fun refreshAsteroidData() {
        withContext(Dispatchers.IO) {
            
            // Get data from api
            val result = NeoApi.retrofitService.getNeoFeedAsync(getCurrentDate(), BuildConfig.NASA_API_KEY)
                .await() // Parse the data from API
            val neoFeed = parseAsteroidsJsonResult(JSONObject(result))
            
            // Transform from ArrayList<Asteroid> to Array<DatabaseAsteroid> and store or refresh all data into DB
            val asteroids = NetworkAsteroidContainer(neoFeed)
            database.dao.insertAll(* asteroids.asDatabaseModel())
        }
    }
    
    //Get a livedata object with the picture info.
    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay> get() = _picture
    
    suspend fun getPictureOfTheDay() {
        withContext(Dispatchers.Main) {
            val result = NeoApi.retrofitService.getImageOfTheDayAsync(BuildConfig.NASA_API_KEY).await()
            _picture.value = result
        }
    }
    
    //Delete asteroid
    suspend fun deleteAsteroidData(){
        withContext(Dispatchers.IO){
            database.dao.deleteAsteroid()
        }
    }
}