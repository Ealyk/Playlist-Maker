package ui

import android.app.Application
import com.ealyk.playlistmaker2.Creator
import domain.api.ThemeSwitcher


class App: Application() {

    private lateinit var provideThemeSwitcher: ThemeSwitcher

    override fun onCreate() {
        super.onCreate()

        provideThemeSwitcher = Creator.provideThemeSwitcher(this)

        val isDark = provideThemeSwitcher.loadTheme()

        provideThemeSwitcher.switchTheme(isDark)


    }

}