package iambedoy.quotegarden.repository

import iambedoy.quotegarden.response.QuoteResponse
import iambedoy.quotegarden.service.QuoteService

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
class QuoteRepository (
    private val service: QuoteService
) {
    suspend fun requestQuote(): QuoteResponse {
        return try {
            val random = service.getRandom()
            random
        }catch (e: Exception){
            e.printStackTrace()
            QuoteResponse(200, quote = null)
        }
    }

}