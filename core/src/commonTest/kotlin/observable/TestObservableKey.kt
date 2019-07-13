package observable

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.instantsearch.core.observable.ObservableKey
import shouldEqual
import kotlin.test.Test


class TestObservableKey {

    @Test
    fun invokeObservableItemShouldGenerateKeyName() {
        val item: ObservableItem<String?> = ObservableItem(null)

        repeat(10) {
            ObservableKey(item).name shouldEqual item.listeners.size.toString()
        }
    }

    @Test
    fun invokeObservableEventShouldGenerateKeyName() {
        val item: ObservableEvent<Unit?> = ObservableEvent()

        repeat(10) {
            ObservableKey(item).name shouldEqual item.listeners.size.toString()
        }
    }
}