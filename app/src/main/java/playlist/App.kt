package playlist

import android.app.Application
import appModule
import historyModule
import navigationModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import playerModule
import playlist.settings.domain.Impl.ThemeClientImpl
import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.api.ThemeClient
import searchModule
import settingsModule
import sharingModule


class App: Application() {

    private val settingsInteractor: SettingsInteractor by inject()
    private lateinit var themeClient: ThemeClient

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule,
                navigationModule,
                historyModule,
                settingsModule,
                sharingModule,
                searchModule,
                playerModule,
            )
        }
        themeClient = ThemeClientImpl()

        val savedTheme = settingsInteractor.getTheme()
        themeClient.applyTheme(savedTheme)

    }

}