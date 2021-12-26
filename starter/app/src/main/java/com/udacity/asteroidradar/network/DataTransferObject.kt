package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.database.DataBaseAsteroid
import com.udacity.asteroidradar.model.Asteroid

data class NetworkAsteroidContainer(val asteroids: List<Asteroid>)

data class NetworkAsteroid(val id: Long,
                           val codename: String,
                           val closeApproachDate: String,
                           val absoluteMagnitude: Double,
                           val estimatedDiameter: Double,
                           val relativeVelocity: Double,
                           val distanceFromEarth: Double,
                           val isPotentiallyHazardous: Boolean)


fun NetworkAsteroidContainer.asDatabaseModel(): Array<DataBaseAsteroid> {
    return asteroids.map {
        DataBaseAsteroid(id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
        .toTypedArray()
}
