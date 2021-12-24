package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Init repo
    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)
    
    // List of asteroids
    val asteroids: LiveData<List<Asteroid>> = repository.asteroidsData
    
    private val _dayPicture = MutableLiveData<PictureOfDay>()
    val dayPicture: LiveData<PictureOfDay> get() = _dayPicture
    
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
    
    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                _dayPicture.value = NeoApi.retrofitService
                    .getImageOfTheDayAsync(Constants.API_KEY).await()
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
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