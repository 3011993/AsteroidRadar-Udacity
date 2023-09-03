package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabaseEntities
import com.udacity.asteroidradar.database.DatabaseForImages
import kotlin.time.measureTimedValue

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids : List<Asteroids>)


@JsonClass(generateAdapter = true)
data class Asteroids(val id: Long, val codename: String, val closeApproachDate: String,
                     val absoluteMagnitude: Double, val estimatedDiameter: Double,
                     val relativeVelocity: Double, val distanceFromEarth: Double,
                     val isPotentiallyHazardous: Boolean)



fun ArrayList<Asteroid>.asDatabaseModel(): Array<AsteroidDatabaseEntities> {
    return map {
        AsteroidDatabaseEntities(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()

}


    @JsonClass(generateAdapter = true)
    data class ImageObjectContainer(@Json(name = "media_type") val mediaType: String, val title: String,
                            val url: String)


   fun DatabaseForImages.asImageDomainModel() : PictureOfDay {
       return let {
           PictureOfDay(
               mediaType = it.mediatype,
               url = it.url,
               title = it.title
           )
       }
    }

fun ImageObjectContainer.asDatabaseModel(): DatabaseForImages {
    return let {
        DatabaseForImages(
            id = 1,
            url = it.url,
            mediatype = it.mediaType,
            title = it.title
        )
    }
}


