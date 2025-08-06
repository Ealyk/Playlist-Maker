package playlist.settings.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.model.ThemeSettings
import playlist.sharing.domain.SharingInteractor


class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {


    private val themeLiveData = MutableLiveData<ThemeSettings>()
    fun observeTheme(): LiveData<ThemeSettings> = themeLiveData

    fun changeTheme(isDarkTheme: Boolean) {
        settingsInteractor.setTheme(ThemeSettings(isDarkTheme))
    }

    fun getCurrentTheme(): ThemeSettings {
        return settingsInteractor.getTheme()
    }


    fun onShareClicked() = sharingInteractor.shareApp()
    fun onTermsClicked() = sharingInteractor.openTerms()
    fun onSupportClicked() = sharingInteractor.openSupport()

}