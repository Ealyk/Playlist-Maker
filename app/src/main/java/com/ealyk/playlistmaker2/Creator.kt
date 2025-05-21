package com.ealyk.playlistmaker2

import android.content.Context
import android.os.Handler
import data.AudioPlayerImpl
import data.HistoryRepositoryImpl
import data.ThemeRepositoryImpl
import data.TrackRepositoryImpl
import data.network.RetrofitNetworkClient
import domain.api.AudioPlayer
import domain.api.HistoryInteractor
import domain.api.HistoryRepository
import domain.api.ThemeRepository
import domain.api.ThemeSwitcher
import domain.api.TrackInteractor
import domain.api.TrackRepository
import domain.impl.HistoryInteractorImpl
import domain.impl.ThemeSwitcherImpl
import domain.impl.TrackInteractorImpl

object Creator {

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }
    private fun getThemeRepository(context: Context): ThemeRepository {
        return ThemeRepositoryImpl(context)
    }

    fun provideThemeSwitcher(context: Context): ThemeSwitcher {
        return ThemeSwitcherImpl(getThemeRepository(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    fun provideAudioPlayer(handler: Handler, updateCallback: (String) -> Unit): AudioPlayer {
        return AudioPlayerImpl(handler, updateCallback)
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

}