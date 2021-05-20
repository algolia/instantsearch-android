package number

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.range.Range
import shouldEqual
import kotlin.test.Test

class TestNumberViewModel {

    @Test
    fun coerceShouldNotCoerceIfNoBounds() {
        val viewModel = NumberViewModel<Int>()
        val value = -1

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(value)
        viewModel.number.value shouldEqual value
    }

    @Test
    fun coerceShouldCoerceMin() {
        val range = 0..10
        val viewModel = NumberViewModel(range)

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(-1)
        viewModel.number.value shouldEqual range.first
    }

    @Test
    fun coerceShouldCoerceMax() {
        val range = 0..10
        val viewModel = NumberViewModel(range)

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(11)
        viewModel.number.value shouldEqual range.last
    }

    @Test
    fun changeMinShouldCoerceNumber() {
        val range = 0..10
        val viewModel = NumberViewModel(range)
        val value = 5

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(value)
        viewModel.number.value shouldEqual value
        viewModel.bounds.value = Range(6..10)
        viewModel.number.value shouldEqual 6
    }

    @Test
    fun changeMaxShouldCoerceNumber() {
        val range = 0 until 10
        val viewModel = NumberViewModel(range)
        val value = 5

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        viewModel.coerce(value)
        viewModel.number.value shouldEqual value
        viewModel.bounds.value = Range(0..4)
        viewModel.number.value shouldEqual 4
    }
}
