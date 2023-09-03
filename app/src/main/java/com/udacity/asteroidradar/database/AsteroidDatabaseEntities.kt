package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity
data class AsteroidDatabaseEntities
    (
    @PrimaryKey val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)


fun List<AsteroidDatabaseEntities>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }


}

@Entity
data class DatabaseForImages (
    @PrimaryKey val id: Long,
    val mediatype: String,
    val title: String,
    val url: String
)


fun DatabaseForImages.asImageDomainModel(): PictureOfDay {
    return let {
        PictureOfDay(
            mediaType = it.mediatype,
            title = it.title,
            url = it.url
        )

    }
}

