package playlist.settings.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playlist.creator.Creator
import playlist.settings.domain.model.ThemeSettings



class SettingsViewModel: ViewModel() {

    private val settingsInteractor = Creator.provideSettingsInteractor()
    private val sharingInteractor = Creator.provideSharingInteractor()

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