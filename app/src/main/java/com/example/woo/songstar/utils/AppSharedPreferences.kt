package com.example.woo.songstar.utils

import android.content.Context
import androidx.preference.PreferenceManager

object AppSharedPreferences {
    const val USER_ID = "user_id"
    const val NAME = "name"
    const val USERNAME = "userName"
    const val LANGUAGE = "language"
    const val PHOTO = "photo"
    const val IS_ADMIN = "isAdmin"

    fun putString(context: Context?, key: String?, value: String?) {
        try {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getString(context: Context?, key: String?): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }

    fun putBoolean(context: Context?, key: String?, value: Boolean) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context?, key: String?): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean(key, false)
    }

    fun clearAllPreferences(context: Context?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}