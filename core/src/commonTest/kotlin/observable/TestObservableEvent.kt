package observable

import com.algolia.instantsearch.core.observable.ObservableEvent
import shouldEqual
import kotlin.test.Test


class TestObservableEvent {

    private val value = "value"

    @Test
    fun sendValueShouldCallListeners() {
        var expected: String? = null
        val item: ObservableEvent<String?> = ObservableEvent()

        item.subscribe { expected = it }
        item.send(value)
        expected shouldEqual value
    }

    @Test
    fun unsubscribeShouldRemoveListener() {
        val item: ObservableEvent<String?> = ObservableEvent()
        val listener: (String?) -> Unit = { }

        item.subscribe(listener)
        item.subscribe(listener)
        item.listeners shouldEqual listOf(listener, listener)
        item.unsubscribe(listener)
        item.listeners shouldEqual listOf(listener)
    }

    @Test
    fun unsubscribeAllShouldRemoveListeners() {
        val item: ObservableEvent<String?> = ObservableEvent()
        val listener: (String?) -> Unit = { }

        item.subscribe(listener)
        item.subscribe(listener)
        item.listeners shouldEqual listOf(listener, listener)
        item.unsubscribeAll()
        item.listeners shouldEqual emptyList()
    }
}