package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.network.NeoApi
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {
    
    // List of asteroids
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() = _asteroids
    
    private val _dayPicture = MutableLiveData<PictureOfDay>()
    val dayPicture: LiveData<PictureOfDay> get() = _dayPicture
    
    //Navigation value
    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail
    
    init {
        getImageOfTheDay()
        getNeoFee()
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
                _dayPicture.value = NeoApi.retrofitService.getImageOfTheDayAsync(Constants.API_KEY)
                    .await()
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    
    private fun getNeoFee() {
        viewModelScope.launch {
            try {
                val result = NeoApi.retrofitService.getNeoFeedAsync("2021-12-17", Constants.API_KEY)
                    .await()
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(result))
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    
    private fun setImageOnTheDay() {
    
    }
    
    
    //TODO: delete.
    //    private fun getDummyList(): List<Asteroid> {
    //        return mutableListOf(Asteroid(49,
    //                "Prueba (12323-)",
    //                "12-3-2021",
    //                59595.00,
    //                6969696.56,
    //                454.68,
    //                435545.00,
    //                true),
    //                Asteroid(49,
    //                        "Test (987-65)",
    //                        "12-3-2021",
    //                        59595.00,
    //                        6969696.56,
    //                        454.68,
    //                        435545.00,
    //                        true),
    //                Asteroid(49,
    //                        "Change (12323-)",
    //                        "12-3-2021",
    //                        59595.00,
    //                        6969696.56,
    //                        454.68,
    //                        435545.00,
    //                        true))
    //    }
}