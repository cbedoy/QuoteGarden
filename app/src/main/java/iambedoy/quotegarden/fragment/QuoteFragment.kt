package iambedoy.quotegarden.fragment

import android.app.AlertDialog
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import iambedoy.quotegarden.R
import iambedoy.quotegarden.response.Quote
import iambedoy.quotegarden.viewmodel.QuoteViewModel
import kotlinx.android.synthetic.main.fragment_quote.*
import org.koin.android.ext.android.inject
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Random Jokes
 *
 * Created by bedoy on 01/07/20.
 */
class QuoteFragment : Fragment(), SensorEventListener{

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private val threshold = 5.0 // m/S**2
    private val timeOffset = 3500

    private var lastShakeTime : Long = 0

    private val colors = listOf(
        R.color.color_primary_01,
        R.color.color_primary_02,
        R.color.color_primary_03,
        R.color.color_primary_04,
        R.color.color_primary_05,
        R.color.color_primary_06,
        R.color.color_primary_07
    )

    private val aboutMessage : TextView
        get() {
            val textView = TextView(context)
            val dp = 24.px
            val spannableString = SpannableString(
                "Shake your phone and have fun. \n\n"+
                        "Develop:\n" +
                        "Carlos Bedoy | Android Engineer \n\n" +
                        "https://www.linkedin.com/in/carlos-bedoy-34248187\n" +
                        "http://cbedoy.github.io\n" +
                        "\n" +
                        "Backend: \n" +
                        "QuoteGarden\n\nhttps://github.com/pprathameshmore/QuoteGarden\n\n" +
                        getString(R.string.credit) +"\n" +
                        getString(R.string.credit_alt) +"\n"

            )
            Linkify.addLinks(spannableString, Linkify.WEB_URLS)
            textView.text = spannableString
            textView.setTextColor(Color.parseColor("#282a36"))
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.setPadding(dp,dp,dp,dp)

            return textView
        }

    private val viewModel : QuoteViewModel by inject()

    private val sensorManager by lazy {
        context?.getSystemService(SENSOR_SERVICE) as? SensorManager
    }

    private val sensor by lazy {
        sensorManager?.getDefaultSensor(TYPE_ACCELEROMETER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_quote, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.currentQuote.observe(viewLifecycleOwner, Observer { quote ->

            context?.let {
                title_view.setTextColor(ContextCompat.getColor(it, colors.shuffled().first()))
            }

            val hashTag = getString(if(quote.quoteText.length % 2 == 0) R.string.credit else R.string.credit_alt)
            genre_view.text = "#${quote.quoteGenre}"
            title_view.text = quote.quoteText
            description_view.text = quote.quoteAuthor
            joke_hash_tag.text = hashTag

            title_view.setOnClickListener { shareContent(quote) }
            description_view.setOnClickListener { shareContent(quote) }
        })


    }

    private fun shareContent(quote: Quote) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${quote.quoteText}\n- ${quote.quoteAuthor}\n#${quote.quoteGenre}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onResume() {
        super.onResume()

        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        viewModel.takeRandomQuote()
    }

    override fun onPause() {
        super.onPause()

        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quote_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_info -> {
                AlertDialog.Builder(activity)
                    .setTitle("About")
                    .setView(aboutMessage)
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent?.let {
            if (sensor?.type == TYPE_ACCELEROMETER) {
                val curTime = System.currentTimeMillis()
                if (curTime - lastShakeTime > timeOffset) {
                    val x: Float = sensorEvent.values[0]
                    val y: Float = sensorEvent.values[1]
                    val z: Float = sensorEvent.values[2]
                    val acceleration = sqrt(
                        x.toDouble().pow(2.0) +
                                y.toDouble().pow(2.0) +
                                z.toDouble().pow(2.0)
                    ) - SensorManager.GRAVITY_EARTH
                    Log.d("JokeFragment", "${acceleration * 0.3} -> $threshold")
                    if (acceleration * 0.3 > threshold) {
                        //Log.d("JokeFragment", "Shake, Rattle, and Roll")

                        lastShakeTime = curTime

                        viewModel.takeRandomQuote()
                    }
                }
            }
        }
    }


}