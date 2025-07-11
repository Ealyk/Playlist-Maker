package playlist.settings.data.impl

import android.content.Context

import playlist.settings.domain.SettingsRepository
import playlist.settings.domain.model.ThemeSettings


class SettingsRepositoryImpl(private val context: Context): SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)


    override fun getThemeSettings(): ThemeSettings {
        val theme = sharedPreferences.getBoolean(SWITCH_THEME_KEY, ThemeSettings.DEFAULT_THEME)
        return ThemeSettings(theme)

}
    override fun updateThemeSettings(settings: ThemeSettings) {

        sharedPreferences.edit().putBoolean(SWITCH_THEME_KEY, settings.isDark).apply()

    }

    companion object {
        private const val SHARED_PREF_KEY = "shared key"
        private const val SWITCH_THEME_KEY = "switch key"
    }

}