package playlist

import android.app.Application
import playlist.creator.Creator
import playlist.settings.domain.Impl.ThemeClientImpl
import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.api.ThemeClient


class App: Application() {

    private lateinit var provideSettingsRepository: SettingsInteractor
    private lateinit var themeClient: ThemeClient

    override fun onCreate() {
        super.onCreate()

        Creator.init(this)
        provideSettingsRepository = Creator.provideSettingsInteractor()
        themeClient = ThemeClientImpl()

        val savedTheme = provideSettingsRepository.getTheme()
        themeClient.applyTheme(savedTheme)

    }

}