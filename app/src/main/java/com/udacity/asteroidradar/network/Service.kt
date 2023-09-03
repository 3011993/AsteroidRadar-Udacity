package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class MarsApiFilter(val value: String) {
    SHOW_RENT("start_date"), SHOW_BUY("end_date"), SHOW_ALL("api_key")

}

interface AsteroidService {

    @GET("/neo/rest/v1/feed")
    fun getAsteroids(@Query("api_key") apiKey: String): Deferred<ResponseBody>


    @GET("/planetary/apod")
    fun getImage(@Query("api_key") apiKey: String): Deferred<ImageObjectContainer>

}

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


object Network {

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

    val asteroidService = retrofit.create(AsteroidService::class.java)

}