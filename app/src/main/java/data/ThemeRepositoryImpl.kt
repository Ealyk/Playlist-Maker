package data

import android.content.Context
import domain.api.ThemeRepository


class ThemeRepositoryImpl(private val context: Context): ThemeRepository {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)

    override fun saveTheme(isDarkMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(SWITCH_THEME_KEY, isDarkMode)
            .apply()
    }

    override fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_THEME_KEY, DEF_IS_DARK)
    }


    companion object {
        private const val DEF_IS_DARK = false
        private const val SHARED_PREF_KEY = "shared key"
        private const val SWITCH_THEME_KEY = "switch key"
    }


}