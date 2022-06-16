package searcher

import com.algolia.instantsearch.core.searcher.Debouncer
import kotlin.test.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import shouldEqual

class TestDebouncer {

    @Test
    fun shouldDebounce() = runTest {
        val debouncer = Debouncer(100)
        var count = 0
        val block: suspend () -> Unit = { count++ }

        debouncer.debounce(this, block)
        debouncer.debounce(this, block)
        debouncer.debounce(this, block)
        debouncer.job!!.join()
        count shouldEqual 1
    }

    @Test
    fun shouldNotDebounce() = runTest {
        val debouncer = Debouncer(100)
        var count = 0
        val block: suspend () -> Unit = { count++ }

        debouncer.debounce(this, block)
        delay(200)
        debouncer.debounce(this, block)
        delay(200)
        debouncer.debounce(this, block)
        debouncer.job!!.join()
        count shouldEqual 3
    }
}
