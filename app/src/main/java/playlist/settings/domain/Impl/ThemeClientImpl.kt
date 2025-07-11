package playlist.settings.domain.Impl

import androidx.appcompat.app.AppCompatDelegate
import playlist.settings.domain.api.ThemeClient
import playlist.settings.domain.model.ThemeSettings

class ThemeClientImpl: ThemeClient {

    override fun applyTheme(theme: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (theme.isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}