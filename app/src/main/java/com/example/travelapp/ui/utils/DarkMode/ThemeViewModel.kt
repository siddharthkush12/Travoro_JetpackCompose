package com.example.travelapp.ui.utils.DarkMode

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(
    application: Application
) :
    AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    private val _isDark = MutableStateFlow(
        prefs.getBoolean("dark_mode", false)
    )
    val isDark = _isDark.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDark.value
        Log.d("THEME", "Dark mode: $newValue") // 👈 check this

        _isDark.value = newValue
        prefs.edit().putBoolean("dark_mode", newValue).apply()
    }
}