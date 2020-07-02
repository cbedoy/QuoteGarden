package iambedoy.quotegarden.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import iambedoy.quotegarden.repository.QuoteRepository
import iambedoy.quotegarden.response.Quote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
class QuoteViewModel (
    private val repository: QuoteRepository
): ViewModel(){
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val _currentQuote =  MutableLiveData<Quote>()

    val currentQuote : LiveData<Quote>
        get() = _currentQuote


    fun takeRandomQuote() {
        scope.launch {
            repository.requestQuote().quote?.let {
                _currentQuote.postValue(it)
            }
        }
    }
}