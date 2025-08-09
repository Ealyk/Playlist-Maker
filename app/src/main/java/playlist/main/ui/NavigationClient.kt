package playlist.main.ui

sealed class NavigationClient {
    object Search : NavigationClient()
    object Media : NavigationClient()
    object Settings : NavigationClient()
}