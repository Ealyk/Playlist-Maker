package domain.api

import data.ThemeRepositoryImpl

interface ThemeSwitcher {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun loadTheme(): Boolean
}