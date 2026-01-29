package documentation.widget

import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.client.api.SearchClient
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.helper.deserialize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.junit.Ignore

@Ignore
internal class DocHighlighting {

    @Serializable
    data class MyMovie(
        override val _highlightResult: JsonObject?,
    ) : Highlightable {

        public val highlightedTitle: HighlightedString?
            get() = getHighlight("title")

        public val highlightedGenres: List<HighlightedString>?
            get() = getHighlights("genre")
    }

    class MyActivity : AppCompatActivity() {

        val client = SearchClient(
            "YourApplicationID",
            "YourAPIKey"
        )
        val searcher = HitsSearcher(client, "YourIndexName")

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val textViewTitle = TextView(this)
            val textViewGenre = TextView(this)

            searcher.response.subscribe { response ->
                if (response != null) {
                    val hit = response.hits.deserialize(MyMovie.serializer()).first()

                    textViewTitle.text = hit.highlightedTitle?.toSpannedString()
                    textViewGenre.text = hit.highlightedGenres?.toSpannedString(BackgroundColorSpan(Color.YELLOW))
                }
            }

            searcher.searchAsync()
        }

        override fun onDestroy() {
            super.onDestroy()
            searcher.cancel()
        }
    }
}
