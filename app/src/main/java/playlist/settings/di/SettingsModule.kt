import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playlist.settings.data.impl.SettingsRepositoryImpl
import playlist.settings.domain.Impl.SettingsInteractorImpl
import playlist.settings.domain.Impl.ThemeClientImpl
import playlist.settings.domain.SettingsInteractor
import playlist.settings.domain.SettingsRepository
import playlist.settings.domain.api.ThemeClient
import playlist.settings.ui.view_model.SettingsViewModel

val settingsModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    single<ThemeClient> {
        ThemeClientImpl()
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get(), get())
    }
    viewModel<SettingsViewModel> {
        SettingsViewModel(get(), get())
    }
}