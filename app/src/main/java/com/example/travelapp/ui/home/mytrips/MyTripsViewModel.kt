package com.example.travelapp.ui.home.mytrips

import androidx.lifecycle.ViewModel
import com.example.travelapp.data.remote.api.TravelApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyTripsViewModel @Inject constructor(
    private val travelApiService: TravelApiService
): ViewModel(){

}