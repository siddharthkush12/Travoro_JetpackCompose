package com.travoro.app.data.remote.repository

import com.travoro.app.core.network.safeApiCall
import com.travoro.app.data.remote.api.TravelApiService
import javax.inject.Inject

class WeatherRepository
    @Inject
    constructor(
        private val apiService: TravelApiService,
    ) {
        suspend fun getWeather(location: String) =
            safeApiCall {
                apiService.getWeather(
                    location = location,
                )
            }
    }
