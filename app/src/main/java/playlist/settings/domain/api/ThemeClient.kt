package playlist.settings.domain.api

import playlist.settings.domain.model.ThemeSettings

interface ThemeClient {
    fun applyTheme(theme: ThemeSettings)

}