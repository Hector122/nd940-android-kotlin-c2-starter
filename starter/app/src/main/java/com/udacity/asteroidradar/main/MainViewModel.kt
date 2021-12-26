package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Init repo
    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)
    
    // List of asteroids
    val asteroids: LiveData<List<Asteroid>> = repository.asteroidsData
    
    // Picture of the day
    val dayPicture: LiveData<PictureOfDay> = repository.picture
    
    //Navigation value
    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail
    
    init {
        getImageOfTheDay()
        getNeoFeed()
    }
    
    fun navigateToDetailScreen(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }
    
    fun doneNavigationToDetailScreen() {
        _navigateToDetail.value = null
    }
    
    /**
     * Get the Neo webservice image of the day.
     */
    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                repository.getPictureOfTheDay()
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Get the list of asteroids to display
     */
    private fun getNeoFeed() {
        viewModelScope.launch {
            try {
                repository.refreshAsteroidData()
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}