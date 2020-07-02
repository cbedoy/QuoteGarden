package iambedoy.quotegarden.response

import com.squareup.moshi.JsonClass

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
@JsonClass(generateAdapter = true)
data class QuoteResponse(
    val statusCode: Int = 0,
    val quote: Quote? = null
)

@JsonClass(generateAdapter = true)
data class Quote(
    val _id: String = "",
    val quoteText: String = "",
    val quoteAuthor: String = "",
    val quoteGenre: String = ""
)