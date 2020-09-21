package instrumented.stats

import android.os.Build
import android.text.SpannedString
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import instrumented.applicationContext
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual

@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(AndroidJUnit4::class)
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
