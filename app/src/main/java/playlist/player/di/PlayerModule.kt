import android.media.MediaPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import playlist.player.data.AudioPlayerImpl
import playlist.player.domain.AudioPlayer
import playlist.player.ui.view_model.AudioplayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

val playerModule = module {
    factory {
        MediaPlayer()
    }

    single {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }


    factory<AudioPlayer> {
        AudioPlayerImpl(get(), get(), androidContext(), get())
    }

    viewModel<AudioplayerViewModel> {
        AudioplayerViewModel(get())
    }
}