package playlist.settings.domain.model

data class ThemeSettings(val isDark: Boolean = DEFAULT_THEME) {
    companion object {
        const val DEFAULT_THEME = false
    }
}