package playlist.creator

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import playlist.player.data.AudioPlayerImpl
import playlist.history.data.HistoryRepositoryImpl
import playlist.search.data.TrackRepositoryImpl
import playlist.search.data.network.RetrofitNetworkClient
import playlist.player.domain.AudioPlayer
import playlist.history.domain.HistoryInteractor
import playlist.history.domain.HistoryRepository
import playlist.search.domain.TrackInteractor
import playlist.search.domain.TrackRepository
import playlist.history.domain.impl.HistoryInteractorImpl
import playlist.search.data.TrackInteractorImpl
import playlist.settings.data.impl.SettingsRepositoryImpl
import playlist.settings.domain.Impl.SettingsInteractorImpl
import playlist.settings.domain.Impl.ThemeClientImpl
import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.SettingsRepository
import playlist.sharing.data.impl.ExternalNavigatorImpl
import playlist.sharing.domain.ExternalNavigator
import playlist.sharing.domain.SharingInteractor
import playlist.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private lateinit var appContext: Context
    private lateinit var sharedPreferences: SharedPreferences


    fun init(context: Context) {
        appContext = context.applicationContext
        sharedPreferences = appContext.getSharedPreferences("shared key", Context.MODE_PRIVATE)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private val getHistoryRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(sharedPreferences)
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository)
    }

    fun provideAudioPlayer(handler: Handler, updateCallback: (String) -> Unit): AudioPlayer {
        return AudioPlayerImpl(appContext, handler, updateCallback)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    private val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(sharedPreferences)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository, ThemeClientImpl())
    }

    val getExternalNavigator: ExternalNavigator by lazy {
        ExternalNavigatorImpl(appContext)
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(context = appContext, externalNavigator = getExternalNavigator)
    }


}