package com.ealyk.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        darkTheme = sharedPreferences.getBoolean(SWITCH_KEY, DEF_IS_DARK)


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




companion object {
    const val SHARED_PREF_KEY = "shared key"
    const val SWITCH_KEY = "switch key"
    const val DEF_IS_DARK = false
}

}