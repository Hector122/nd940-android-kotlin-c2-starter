package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AppDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.model.Asteroid
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
            val result = NeoApi.retrofitService.getNeoFeedAsync(getCurrentDate(), Constants.API_KEY).await()
            // Parse the data from API
            val neoFeed = parseAsteroidsJsonResult(JSONObject(result))
            
            // Transform from ArrayList<Asteroid> to Array<DatabaseAsteroid> and Store and refresh all data into DB
            val asteroids = NetworkAsteroidContainer(neoFeed)
            database.dao.insertAll(* asteroids.asDatabaseModel())
        }
    }
}