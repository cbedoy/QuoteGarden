package iambedoy.quotegarden.service

import iambedoy.quotegarden.response.QuoteResponse
import retrofit2.http.GET

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
interface QuoteService {
    @GET("quotes/random")
    suspend fun getRandom() : QuoteResponse
}