package playlist.main.ui.impl

import android.content.Context
import android.content.Intent
import playlist.main.ui.NavigationClient
import playlist.settings.ui.activity.SettingsActivity
import playlist.media.ui.activity.MediaActivity
import playlist.search.ui.activity.SearchActivity

class NavigationClientImpl(private val context: Context): NavigationClient {
    override fun onSearchClicked() {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    override fun onMediaClicked() {
        val mediaIntent = Intent(context, MediaActivity::class.java)
        context.startActivity(mediaIntent)
    }

    override fun onSettingsClicked() {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        context.startActivity(settingsIntent)
    }

}