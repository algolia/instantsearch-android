package observable

import com.algolia.instantsearch.core.observable.SubscriptionEvent
import shouldEqual
import kotlin.test.Test


class TestSubscription {

    @Test
    fun unsubscribeShouldRemoveSubscription() {
        val item: SubscriptionEvent<String?> = SubscriptionEvent()
        val listener: (String?) -> Unit = { }

        item.subscribe(listener)
        item.subscriptions shouldEqual setOf(listener)
        item.unsubscribe(listener)
        item.subscriptions shouldEqual emptySet()
    }

    @Test
    fun unsubscribeAllShouldRemoveSubscriptions() {
        val item: SubscriptionEvent<String?> = SubscriptionEvent()
        val listenerA: (String?) -> Unit = { }
        val listenerB: (String?) -> Unit = { }

        item.subscribe(listenerA)
        item.subscribe(listenerB)
        item.subscriptions shouldEqual setOf(listenerA, listenerB)
        item.unsubscribeAll()
        item.subscriptions shouldEqual emptySet()
    }
}