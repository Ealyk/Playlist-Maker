package playlist.settings.domain.Impl

import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.SettingsRepository
import playlist.settings.domain.api.ThemeClient
import playlist.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
    private val themeClient: ThemeClient
): SettingsInteractor {

    override fun getTheme(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun setTheme(themeSettings: ThemeSettings) {
        settingsRepository.updateThemeSettings(themeSettings)
        themeClient.applyTheme(themeSettings)
    }
}