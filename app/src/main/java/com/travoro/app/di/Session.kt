package com.travoro.app.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        fun storeToken(token: String) {
            sharedPref.edit().putString("token", token).apply()
        }

        fun getToken(): String? {
            return sharedPref.getString("token", null)
        }

        fun removeToken() {
            return sharedPref.edit().remove("token").apply()
        }

        fun storeUserId(userId: String) {
            sharedPref.edit().putString("userId", userId).apply()
        }

        fun getUserId(): String? {
            return sharedPref.getString("userId", null)
        }

        fun removeUserId() {
            sharedPref.edit().remove("userId").apply()
        }

        fun storeProfileImage(url: String) {
            sharedPref.edit().putString("profilePic", url).apply()
        }

        fun getProfileImage(): String? {
            return sharedPref.getString("profilePic", null)
        }

        fun removeProfileImage() {
            sharedPref.edit().remove("profilePic").apply()
        }
    }
