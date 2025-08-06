import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playlist.search.data.NetworkClient
import playlist.search.data.TrackRepositoryImpl
import playlist.search.data.network.RetrofitNetworkClient
import playlist.search.data.network.iTunesApi
import playlist.search.domain.TrackInteractor
import playlist.search.domain.TrackInteractorImpl
import playlist.search.domain.TrackRepository
import playlist.search.ui.view_model.SearchViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }
    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }
    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }
}