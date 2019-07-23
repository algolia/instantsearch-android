import com.algolia.instantsearch.core.observable.SubscriptionValue
import kotlin.test.Test


class TestSubscriptionValue {

    private val value = "value"

    @Test
    fun setAndGetShouldWork() {
        val item: SubscriptionValue<String?> = SubscriptionValue(null)

        item.value = value
        item.value shouldEqual value
        item.value shouldEqual value
    }

    @Test
    fun initShouldSetDefaultValue() {
        val item: SubscriptionValue<String?> = SubscriptionValue(value)

        item.value shouldEqual value
    }

    @Test
    fun setValueShouldCallSubscriptions() {
        var expected: String? = null
        val item: SubscriptionValue<String?> = SubscriptionValue(null)

        item.subscribe { expected = it }
        item.value = value
        expected shouldEqual value
    }

    @Test
    fun subscribePastShouldCallSubscriptions() {
        var expected: String? = null
        val item: SubscriptionValue<String?> = SubscriptionValue(value)

        item.subscribePast { expected = value }
        expected shouldEqual value
    }
}