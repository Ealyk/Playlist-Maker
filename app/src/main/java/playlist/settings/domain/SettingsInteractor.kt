package playlist.settings.domain

import playlist.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getTheme(): ThemeSettings
    fun setTheme(themeSettings: ThemeSettings)
}