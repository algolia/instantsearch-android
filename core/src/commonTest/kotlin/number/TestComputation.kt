package number

import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.increment
import shouldEqual
import kotlin.test.Test


class TestComputation {

    @Test
    fun incrementDefaultZero() {
        var value: Int? = null
        val computation: Computation<Int> = { value = it(value) }

        computation.increment()
        value shouldEqual 0
    }

    @Test
    fun incrementDefaultOne() {
        var value: Int? = null
        val computation: Computation<Int> = { value = it(value) }

        computation.increment(default = 1)
        value shouldEqual 1
    }

    @Test
    fun incrementStep() {
        var value: Int? = null
        val step = 2
        val computation: Computation<Int> = { value = it(value) }

        computation.increment(step = step)
        value shouldEqual 0
        computation.increment(step = step)
        value shouldEqual 2
        computation.increment(step = step)
        value shouldEqual 4
    }
}