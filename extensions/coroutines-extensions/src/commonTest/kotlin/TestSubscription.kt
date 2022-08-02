import app.cash.turbine.test
import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.coroutines.asFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalInstantSearch::class, ExperimentalCoroutinesApi::class)
class TestSubscription {

    @Test
    fun testSubscriptionEventAsFlow() = runTest {
        val subscriptionEvent = SubscriptionEvent<String?>()

        subscriptionEvent.asFlow().test {
            // send : "event1"
            launch { subscriptionEvent.send("event1") }
            assertEquals("event1", awaitItem())
            // send : "event2"
            launch { subscriptionEvent.send("event2") }
            assertEquals("event2", awaitItem())
            // wrap up
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSubscriptionValueAsFlow() = runTest {
        val subscriptionEvent = SubscriptionValue<String?>("event0")
        println("start")

        subscriptionEvent.asFlow().test {
            // send : "event1"
            launch { subscriptionEvent.value = "event1" }
            assertEquals("event1", awaitItem())
            // send : "event2"
            launch { subscriptionEvent.value = "event2" }
            assertEquals("event2", awaitItem())
            // wrap up
            cancelAndIgnoreRemainingEvents()
        }
    }
}
