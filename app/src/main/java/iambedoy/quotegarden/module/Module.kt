package iambedoy.quotegarden.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import iambedoy.quotegarden.fragment.QuoteFragment
import iambedoy.quotegarden.repository.QuoteRepository
import iambedoy.quotegarden.service.QuoteService
import iambedoy.quotegarden.viewmodel.QuoteViewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
    .baseUrl("https://quote-garden.herokuapp.com/api/v2/")
    .build()

val fragment = module {
    factory {
        QuoteFragment()
    }
}

val viewModel = module {
    factory {
        QuoteViewModel(get())
    }
}

val repository = module {
    single {
        QuoteRepository(get())
    }
}

val service = module {
    single {
        retrofit.create(QuoteService::class.java)
    }
}

val appModule = fragment + viewModel + repository + service