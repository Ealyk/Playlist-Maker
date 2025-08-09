package playlist.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import playlist.main.ui.NavigationClient


class MainViewModel(): ViewModel() {

    private val _navigateTo = SingleLiveEvent<NavigationClient>()
    fun navigateTo(): LiveData<NavigationClient> = _navigateTo
    fun onSearchClicked() {
        _navigateTo.postValue(NavigationClient.Search)
    }

    fun onMediaClicked() {
        _navigateTo.postValue(NavigationClient.Media)
    }

    fun onSettingsClicked() {
        _navigateTo.postValue(NavigationClient.Settings)
    }

}