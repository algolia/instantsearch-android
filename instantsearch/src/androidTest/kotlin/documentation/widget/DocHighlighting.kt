package documentation.widget

import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.junit.Ignore

@Ignore
class DocHighlighting {

    @Serializable
    data class MyMovie(
        override val _highlightResult: JsonObject?,
    ) : Highlightable {

        public val highlightedTitle: HighlightedString?
            get() = getHighlight(Attribute("title"))

        public val highlightedGenres: List<HighlightedString>?
            get() = getHighlights(Attribute("genre"))
    }

    class MyActivity : AppCompatActivity() {

        val client = ClientSearch(
            ApplicationID("YourApplicationID"),
            APIKey("YourAPIKey")
        )
        val index = client.initIndex(IndexName("YourIndexName"))
        val searcher = SearcherSingleIndex(index)

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
