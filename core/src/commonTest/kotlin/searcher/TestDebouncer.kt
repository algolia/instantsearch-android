package searcher

import blocking
import com.algolia.instantsearch.core.searcher.Debouncer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import shouldEqual
import kotlin.test.Test


class TestDebouncer {

    @Test
    fun shouldDebounce() {
        val debouncer = Debouncer(100)
        var count = 0
        val block: suspend () -> Unit = { count++ }

        blocking {
            debouncer.debounce(this, Dispatchers.Default, block)
            debouncer.debounce(this, Dispatchers.Default, block)
            debouncer.debounce(this, Dispatchers.Default, block)
            debouncer.job!!.join()
            count shouldEqual 1
        }
    }

    @Test
    fun shouldNotDebounce() {
        val debouncer = Debouncer(100)
        var count = 0
        val block: suspend () -> Unit = { count++ }

        blocking {
            debouncer.debounce(this, Dispatchers.Default, block)
            delay(200)
            debouncer.debounce(this, Dispatchers.Default, block)
            delay(200)
            debouncer.debounce(this, Dispatchers.Default, block)
            debouncer.job!!.join()
            count shouldEqual 3
        }
    }
}