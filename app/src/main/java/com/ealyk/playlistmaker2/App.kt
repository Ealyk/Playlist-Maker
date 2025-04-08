package com.ealyk.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.ealyk.playlistmaker2.Constants.DEF_IS_DARK
import com.ealyk.playlistmaker2.Constants.SHARED_PREF_KEY
import com.ealyk.playlistmaker2.Constants.SWITCH_THEME_KEY

class App: Application() {

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