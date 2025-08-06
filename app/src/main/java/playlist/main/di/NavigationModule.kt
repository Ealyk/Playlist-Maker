import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playlist.main.ui.NavigationClient
import playlist.main.ui.view_model.MainViewModel

val navigationModule = module {


    viewModel<MainViewModel> {
        MainViewModel()
    }

}