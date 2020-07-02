package iambedoy.quotegarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iambedoy.quotegarden.fragment.QuoteFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val quoteFragment : QuoteFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, quoteFragment)
            .commit()
    }
}
