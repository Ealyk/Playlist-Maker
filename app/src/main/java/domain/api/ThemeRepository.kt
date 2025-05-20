package domain.api


interface ThemeRepository {
    fun saveTheme(isDarkMode: Boolean)
    fun isDarkMode(): Boolean
}