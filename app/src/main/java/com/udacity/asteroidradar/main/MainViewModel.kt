package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.asImageDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val database = getDatabase(app)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _asteroids = MutableLiveData<List<Asteroid>>()


    val asteroids: LiveData<List<Asteroid>> = _asteroids

    val images = Transformations.map(database.asteroidDao.getImage()) { it?.asImageDomainModel() }


    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshImages()
            getAsteroidOfWeek()
        }
    }


    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()
    val navigateToDetailFragment: LiveData<Asteroid> = _navigateToDetailFragment

    fun displayDetailFragment(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun displayDetailFragmentDone() {
        _navigateToDetailFragment.value = null
    }

    @SuppressLint("NewApi")
    fun getAsteroidOfWeek() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 6)

        val lastDay = dateFormat.format(currentTime)
        getAsteroidsinRange(today, lastDay)

    }

    fun getAllasteroids() {
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroids().collect {
                _asteroids.value = it?.asDomainModel()
            }
        }
    }

    @SuppressLint("NewApi")
    fun getAsteroidOfDay() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        getAsteroidsinRange(today, today)

    }

    private fun getAsteroidsinRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            database.asteroidDao.getAsteroidOfRange(startDate, endDate).collect() {
                _asteroids.value = it?.asDomainModel()
            }
        }

    }


}

class Factory(val app: Application) : ViewModelProvider.Factory {

    //maybe will be something wrong here
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}



