import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import playlist.sharing.data.impl.ExternalNavigatorImpl
import playlist.sharing.domain.ExternalNavigator
import playlist.sharing.domain.SharingInteractor
import playlist.sharing.domain.impl.SharingInteractorImpl

val sharingModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get(), androidContext())
    }
}