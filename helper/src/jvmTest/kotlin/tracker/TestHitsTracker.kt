package tracker

import com.algolia.instantsearch.helper.tracker.internal.HitsDataTracker
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.QueryID
import com.algolia.search.model.indexing.Indexable
import extension.MainCoroutineRule
import extension.runBlocking
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import searcher.TestCoroutineScope
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestHitsTracker {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val eventName = "eventName"
    private val queryID = QueryID("queryID")
    private val trackableSearcher = mockk<TrackableSearcher<*>>(relaxed = true)
    private val searchTrackable = mockk<HitsAfterSearchTrackable>(relaxed = true)
    private val hitsTracker = HitsDataTracker(eventName, trackableSearcher, searchTrackable, TestCoroutineScope)

    @BeforeTest
    fun setup() {
        hitsTracker.queryID = queryID
    }

    @Test
    fun testTrackClick() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val hit = mockk<Indexable>(relaxed = true)
        val position = 10
        val objectIDs = EventObjects.IDs(hit.objectID.raw)
        val positions = listOf(position)

        hitsTracker.trackClick(hit = hit, position = position, customEventName = eventName)

        verify {
            searchTrackable.clickedAfterSearch(
                eventName = eventName,
                queryId = queryID.raw,
                objectIDs = objectIDs,
                positions = positions,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConvert() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val hit = mockk<Indexable>(relaxed = true)
        val objectIDs = EventObjects.IDs(hit.objectID.raw)

        hitsTracker.trackConvert(hit = hit, customEventName = eventName)

        verify {
            searchTrackable.convertedAfterSearch(
                eventName = eventName,
                queryId = queryID.raw,
                objectIDs = objectIDs,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackView() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val hit = mockk<Indexable>(relaxed = true)
        val objectIDs = EventObjects.IDs(hit.objectID.raw)

        hitsTracker.trackView(hit = hit, customEventName = eventName)

        verify {
            searchTrackable.viewed(
                eventName = eventName,
                objectIDs = objectIDs,
                timestamp = any()
            )
        }
    }
}
