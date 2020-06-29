package tracker

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import apiKey
import appID
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.FilterTracker
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import indexName1
import indexName2
import insights
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@SmallTest
@Config(sdk = [Build.VERSION_CODES.Q])
@RunWith(AndroidJUnit4::class)
class FilterTrackerTests {

    val eventName = "event name"
    val client = ClientSearch(applicationID = ApplicationID(appID), apiKey = APIKey(apiKey))

    @Test
    fun testSetupTracker() {
        val index = client.initIndex(IndexName(indexName1))
        val searcher = SearcherSingleIndex(index = index)

        val filtersTracker = FilterTracker(
            eventName = eventName,
            searcher = searcher,
            insights = insights
        )

        assertEquals(eventName, filtersTracker.eventName)
        assert(filtersTracker.trackableSearcher is TrackableSearcher.SingleIndex)
    }

    @Test
    fun testSetupSearcherMultiIndex() {
        val indexQuery1 = IndexQuery(IndexName(indexName1))
        val indexQuery2 = IndexQuery(IndexName(indexName2))
        val searcher = SearcherMultipleIndex(client = client, queries = listOf(indexQuery1, indexQuery2))

        val filtersTracker = FilterTracker(
            eventName = eventName,
            searcher = searcher,
            pointer = 0,
            insights = insights
        )

        assertEquals(eventName, filtersTracker.eventName)
        assert(filtersTracker.trackableSearcher is TrackableSearcher.MultiIndex)
    }
}
