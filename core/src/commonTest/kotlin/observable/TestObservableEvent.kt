package observable

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableKey
import shouldEqual
import kotlin.test.Test


class TestObservableEvent {

    private val keyA = ObservableKey("keyA")
    private val keyB = ObservableKey("keyB")
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

        item.subscribe(keyA, listener)
        item.subscribe(keyB, listener)
        item.listeners shouldEqual mapOf(keyA to listener, keyB to listener)
        item.unsubscribe(keyA)
        item.listeners shouldEqual mapOf(keyB to listener)
    }

    @Test
    fun unsubscribeAllShouldRemoveListeners() {
        val item: ObservableEvent<String?> = ObservableEvent()
        val listener: (String?) -> Unit = { }

        item.subscribe(keyA, listener)
        item.subscribe(keyB, listener)
        item.listeners shouldEqual mapOf(keyA to listener, keyB to listener)
        item.unsubscribeAll()
        item.listeners shouldEqual mapOf<String, (String?) -> Unit>()
    }
}