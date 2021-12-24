package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.model.Asteroid


@Entity
data class DataBaseAsteroid(@PrimaryKey val id: Long,
                            val codename: String,
                            @ColumnInfo(name = "close_approach_date") val closeApproachDate: String,
                            @ColumnInfo(name = "absolute_magnitude") val absoluteMagnitude: Double,
                            @ColumnInfo(name = "estimated_diameter") val estimatedDiameter: Double,
                            @ColumnInfo(name = "relativeV_velocity") val relativeVelocity: Double,
                            @ColumnInfo(name = "distance_from_earth") val distanceFromEarth: Double,
                            @ColumnInfo(name = "is_potentially_hazardous") val isPotentiallyHazardous: Boolean)


// Extension fun to convert form DB Obj to Domain Obj
fun List<DataBaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
    }
}