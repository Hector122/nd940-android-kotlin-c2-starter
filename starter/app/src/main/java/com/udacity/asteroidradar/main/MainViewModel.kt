package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    
    // List of asteroids
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() = _asteroids
    
    
    //Navigation value
    private val _navigateToDetail = MutableLiveData<Asteroid>()
    val navigateToDetail: LiveData<Asteroid> get() = _navigateToDetail
    
    init {
        _asteroids.value = getDummyList()
    }
    
    fun navigateToDetailScreen(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }
    
    fun doneNavigationToDetailScreen() {
        _navigateToDetail.value = null
    }
    
    private fun getDummyList(): List<Asteroid> {
        return mutableListOf(Asteroid(49,
                "Prueba (12323-)",
                "12-3-2021",
                59595.00,
                6969696.56,
                454.68,
                435545.00,
                true),
                Asteroid(49,
                        "Test (987-65)",
                        "12-3-2021",
                        59595.00,
                        6969696.56,
                        454.68,
                        435545.00,
                        true),
                Asteroid(49,
                        "Change (12323-)",
                        "12-3-2021",
                        59595.00,
                        6969696.56,
                        454.68,
                        435545.00,
                        true))
    }
    
}