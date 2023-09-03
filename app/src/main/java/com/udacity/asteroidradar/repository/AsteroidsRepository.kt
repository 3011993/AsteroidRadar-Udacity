package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidDatabaseEntities
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.asImageDomainModel
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository (private val database: AsteroidDatabase) {
    private val apiKey = "5EyK5AepfvAb0af5n3gWlGrWyKQ65bDhSzt9LHCp"
    @SuppressLint("NewApi")
    private val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private val currentTime = calendar.time
    private val startDate = dateFormat.format(currentTime)






    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids =
                    Network.asteroidService.getAsteroids(apiKey).await().string()

                val asteroidsparsed = parseAsteroidsJsonResult(JSONObject(asteroids))
                val asteroidslits = asteroidsparsed.asDatabaseModel()
                database.asteroidDao.insertAllAsteroids(*asteroidslits)
            }
            catch (e : Exception) {
                Timber.i(e.message)

            }
        }
    }


    suspend fun refreshImages() {
        withContext(Dispatchers.IO) {
            try {
                val image = Network.asteroidService.getImage(apiKey).await()
                database.asteroidDao.insertImage(image.asDatabaseModel())
            }
            catch (e : Exception) {
                Timber.i(e.message)
            }
        }


    }


}