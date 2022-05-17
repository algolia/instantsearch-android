import app.cash.turbine.test
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.coroutines.asFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

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
}
