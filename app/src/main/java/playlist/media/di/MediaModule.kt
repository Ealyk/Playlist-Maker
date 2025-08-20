import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playlist.media.ui.view_model.FavoritesViewModel
import playlist.media.ui.view_model.PlaylistViewModel

val mediaModule = module {
    viewModel { PlaylistViewModel() }
    viewModel { FavoritesViewModel() }
}