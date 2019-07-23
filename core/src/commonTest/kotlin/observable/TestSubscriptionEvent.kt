package observable

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import shouldEqual
import kotlin.test.Test


class TestSubscriptionEvent {

    private val value = "value"

    @Test
    fun sendValueShouldCallSubscription() {
        var expected: String? = null
        val item: SubscriptionEvent<String?> = SubscriptionEvent()

        item.subscribe { expected = it }
        item.send(value)
        expected shouldEqual value
    }
}