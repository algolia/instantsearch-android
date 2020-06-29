package tracker

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import apiKey
import appID
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.QueryID
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
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
class HitsTrackerTests {

    val eventName = "event name"
    val client = ClientSearch(applicationID = ApplicationID(appID), apiKey = APIKey(apiKey))

    @Test
    fun testSetupSearcherSingleIndex() {
        val index = client.initIndex(IndexName(indexName1))
        val searcher = SearcherSingleIndex(index = index)
        val queryID = QueryID("queryID")

        val hitsTracker = HitsTracker(
            eventName = eventName,
            searcher = searcher,
            insights = insights
        )
        searcher.response.value = ResponseSearch(queryIDOrNull = queryID)

        assertEquals(eventName, hitsTracker.eventName)
        assert(hitsTracker.trackableSearcher is TrackableSearcher.SingleIndex)
        assertEquals(queryID, hitsTracker.queryID)
    }

    @Test
    fun testSetupSearcherMultiIndex() {
        val indexQuery1 = IndexQuery(IndexName(indexName1))
        val indexQuery2 = IndexQuery(IndexName(indexName2))
        val searcher = SearcherMultipleIndex(client = client, queries = listOf(indexQuery1, indexQuery2))

        val hitsTracker = HitsTracker(
            eventName = eventName,
            searcher = searcher,
            pointer = 0,
            insights = insights
        )

        assertEquals(eventName, hitsTracker.eventName)
        assert(hitsTracker.trackableSearcher is TrackableSearcher.MultiIndex)
    }
}
