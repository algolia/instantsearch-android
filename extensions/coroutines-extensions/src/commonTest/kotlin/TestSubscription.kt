import app.cash.turbine.test
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.coroutines.asFlow
import com.algolia.instantsearch.coroutines.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.isActive


@OptIn(ExperimentalInstantSearch::class, ExperimentalCoroutinesApi::class)
class TestSubscription {

    @Test
    fun testSubscriptionEventAsFlow() = runTest {
        val subscriptionEvent = SubscriptionEvent<String?>()
        subscriptionEvent.asFlow().test {
            // send : "event1"
            subscriptionEvent.send("event1")
            assertEquals("event1", awaitItem())
            // send : "event2"
            subscriptionEvent.send("event2")
            assertEquals("event2", awaitItem())
            // wrap up
            cancel()
        }
    }

    @Test
    fun testSubscriptionValueAsFlow() = runTest {
        val subscriptionEvent = SubscriptionValue<String?>("event0")
        subscriptionEvent.asFlow().test {
            // send : "event1"
            subscriptionEvent.value = "event1"
            assertEquals("event1", awaitItem())
            // send : "event2"
            subscriptionEvent.value = "event2"
            assertEquals("event2", awaitItem())
            // wrap up
            cancel()
        }
    }

    @Test
    fun testSubscriptionValueToStateFlow() = runTest {
        val subscriptionEvent = SubscriptionValue<String?>("event0")
        val job = Job()
        val scope = CoroutineScope(job)
        val flow = subscriptionEvent.stateIn(scope)
        flow.test {
            assertEquals("event0", awaitItem())
            // send : "event1"
            subscriptionEvent.value = "event1"
            assertEquals("event1", awaitItem())
            assertEquals("event1", flow.value)
            // send : "event2"
            subscriptionEvent.value = "event2"
            assertEquals("event2", awaitItem())
            assertEquals("event2", flow.value)
            // wrap up
            cancel()
        }
        job.cancelAndJoin()
        subscriptionEvent.value = "event3"
        assertEquals("event2", flow.value)
    }
}
