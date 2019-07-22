package stats

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.stats.StatsTextView
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestStatsTextView {

    private fun view() = StatsTextView(TextView(applicationContext))

    private val text = "text"

    @Test
    fun setItemShouldSetText() {
        val view = view()

        view.setText(text)
        view.view.text shouldEqual text
    }
}