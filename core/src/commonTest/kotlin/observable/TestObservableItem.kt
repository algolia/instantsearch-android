import com.algolia.instantsearch.core.observable.ObservableItem
import com.algolia.instantsearch.core.observable.ObservableKey
import kotlin.test.Test

class TestObservableItem {

    private val keyA = ObservableKey("keyA")
    private val keyB = ObservableKey("keyB")
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

        item.subscribe(keyA, listener)
        item.subscribe(keyB, listener)
        item.listeners shouldEqual mapOf(keyA to listener, keyB to listener)
        item.unsubscribe(keyA)
        item.listeners shouldEqual mapOf(keyB to listener)
    }

    @Test
    fun unsubscribeAllShouldRemoveListeners() {
        val item: ObservableItem<String?> = ObservableItem(null)
        val listener: (String?) -> Unit = { }

        item.subscribe(keyA, listener)
        item.subscribe(keyB, listener)
        item.listeners shouldEqual mapOf(keyA to listener, keyB to listener)
        item.unsubscribeAll()
        item.listeners shouldEqual mapOf<String, (String?) -> Unit>()
    }
}