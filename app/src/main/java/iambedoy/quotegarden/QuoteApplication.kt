package iambedoy.quotegarden

import android.app.Application
import iambedoy.quotegarden.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
class QuoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@QuoteApplication)
            modules(appModule)
        }
    }
}