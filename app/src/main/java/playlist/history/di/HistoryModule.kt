import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import playlist.history.data.HistoryRepositoryImpl
import playlist.history.domain.HistoryInteractor
import playlist.history.domain.HistoryRepository
import playlist.history.domain.impl.HistoryInteractorImpl

val historyModule = module {

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
}