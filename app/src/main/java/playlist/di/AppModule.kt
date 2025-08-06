import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single {
        androidContext()
            .getSharedPreferences("shared key", Context.MODE_PRIVATE)
    }
    factory {
        Gson()
    }
    factory {
        Handler(Looper.getMainLooper())
    }
}