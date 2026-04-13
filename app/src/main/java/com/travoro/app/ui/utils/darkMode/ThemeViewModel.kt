package com.travoro.app.ui.utils.darkMode

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    private val _isDark = MutableStateFlow(prefs.getBoolean("dark_mode", false))
    val isDark = _isDark.asStateFlow()
    fun toggleTheme() {
        val newValue = !_isDark.value
        _isDark.value = newValue
        prefs.edit { putBoolean("dark_mode", newValue) }
    }
}
