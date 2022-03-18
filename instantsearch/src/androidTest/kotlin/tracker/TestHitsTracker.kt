package tracker

import com.algolia.instantsearch.tracker.internal.HitsDataTracker
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.search.model.QueryID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.insights.EventName
import extension.MainCoroutineRule
import extension.runBlocking
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestHitsTracker {

    @get:Rule
    val coroutineRule = MainCoroutineRule()
    private val testCoroutineScope = CoroutineScope(coroutineRule.testDispatcher)

    private val eventName = EventName("eventName")
    private val queryID = QueryID("queryID")
    private val trackableSearcher = mockk<TrackableSearcher<*>>(relaxed = true)
    private val searchTrackable = mockk<HitsAfterSearchTrackable>(relaxed = true)
    private val hitsTracker = HitsDataTracker(eventName, trackableSearcher, searchTrackable, testCoroutineScope)

    @BeforeTest
    fun setup() {
        hitsTracker.queryID = queryID
    }

    @Test
    fun testTrackClick() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val hit = mockk<Indexable>(relaxed = true)
        val position = 10
        val objectIDs = listOf(hit.objectID)
        val positions = listOf(position)

        hitsTracker.trackClick(hit = hit, position = position, customEventName = eventName)

        verify {
            searchTrackable.clickedObjectIDsAfterSearch(
                eventName = eventName,
                queryID = queryID,
                objectIDs = objectIDs,
                positions = positions,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConvert() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val hit = mockk<Indexable>(relaxed = true)
        val objectIDs = listOf(hit.objectID)

        hitsTracker.trackConvert(hit = hit, customEventName = eventName)

        verify {
            searchTrackable.convertedObjectIDsAfterSearch(
                eventName = eventName,
                queryID = queryID,
                objectIDs = objectIDs,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackView() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val hit = mockk<Indexable>(relaxed = true)
        val objectIDs = listOf(hit.objectID)

        hitsTracker.trackView(hit = hit, customEventName = eventName)

        verify {
            searchTrackable.viewedObjectIDs(
                eventName = eventName,
                objectIDs = objectIDs,
                timestamp = any()
            )
        }
    }
}
