import com.algolia.instantsearch.core.observable.ObservableItem
import kotlin.test.Test


class TestObservableItem {

    private val value = "value"

    @Test
    fun setAndGetShouldWork() {
        val item: ObservableItem<String?> = ObservableItem(null)

        item.set(value)
        item.value shouldEqual value
        item.get() shouldEqual value
    }

    @Test
    fun initShouldSetDefaultValue() {
        val item: ObservableItem<String?> = ObservableItem(value)

        item.value shouldEqual value
    }

    @Test
    fun setValueShouldCallListeners() {
        var expected: String? = null
        val item: ObservableItem<String?> = ObservableItem(null)

        item.subscribe { expected = it }
        item.set(value)
        expected shouldEqual value
    }

    @Test
    fun subscribePastShouldCallListeners() {
        var expected: String? = null
        val item: ObservableItem<String?> = ObservableItem(value)

        item.subscribePast { expected = value }
        expected shouldEqual value
    }

    @Test
    fun unsubscribeShouldRemoveListener() {
        val item: ObservableItem<String?> = ObservableItem(null)
        val listener: (String?) -> Unit = { }

        item.subscribe(listener)
        item.subscribe(listener)
        item.listeners shouldEqual listOf(listener, listener)
        item.unsubscribe(listener)
        item.listeners shouldEqual listOf(listener)
    }

    @Test
    fun unsubscribeAllShouldRemoveListeners() {
        val item: ObservableItem<String?> = ObservableItem(null)
        val listener: (String?) -> Unit = { }

        item.subscribe(listener)
        item.subscribe(listener)
        item.listeners shouldEqual listOf(listener, listener)
        item.unsubscribeAll()
        item.listeners shouldEqual emptyList()
    }
}