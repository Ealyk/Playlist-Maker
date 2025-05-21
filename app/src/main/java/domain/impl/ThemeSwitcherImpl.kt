package domain.impl

import androidx.appcompat.app.AppCompatDelegate
import domain.api.ThemeRepository
import domain.api.ThemeSwitcher

class ThemeSwitcherImpl(
    private val themeRepository: ThemeRepository
) : ThemeSwitcher {


    override fun switchTheme(darkThemeenabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeenabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeRepository.saveTheme(darkThemeenabled)
    }

    override fun loadTheme(): Boolean {
        return themeRepository.isDarkMode()
    }
}