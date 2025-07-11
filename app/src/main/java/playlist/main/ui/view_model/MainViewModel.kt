package playlist.main.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import playlist.main.ui.NavigationClient


class MainViewModel(
    private val navigationClient: NavigationClient
): ViewModel() {


    fun onSearchClicked() {
        navigationClient.onSearchClicked()
    }

    fun onMediaClicked() {
        navigationClient.onMediaClicked()
    }

    fun onSettingsClicked() {
        navigationClient.onSettingsClicked()
    }

    companion object {
        fun getFactory(navigationClient: NavigationClient): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(navigationClient)
            }
        }
    }

}