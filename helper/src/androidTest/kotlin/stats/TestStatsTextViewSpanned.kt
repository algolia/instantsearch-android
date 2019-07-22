package stats

import android.text.SpannedString
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import applicationContext
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestStatsTextViewSpanned {

    private fun view() = StatsTextViewSpanned(TextView(applicationContext))

    private val spanned = SpannedString("text")

    @Test
    fun setItemShouldSetText() {
        val view = view()

        view.setText(spanned)
        view.view.text shouldEqual spanned
    }
}