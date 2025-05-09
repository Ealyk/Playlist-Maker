package com.ealyk.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class App: Application() {

    companion object {
        private const val DEF_IS_DARK = false
        private const val SHARED_PREF_KEY = "shared key"
        private const val SWITCH_THEME_KEY = "switch key"
    }

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        darkTheme = sharedPreferences.getBoolean(SWITCH_THEME_KEY, DEF_IS_DARK)


        switchTheme(darkTheme)


    }



    fun switchTheme(darkThemeenabled: Boolean) {
        darkTheme = darkThemeenabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeenabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }






}