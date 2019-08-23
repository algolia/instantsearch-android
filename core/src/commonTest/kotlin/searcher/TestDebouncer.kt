package searcher

import blocking
import com.algolia.instantsearch.core.searcher.Debouncer
import kotlinx.coroutines.CoroutineExceptionHandler
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
            debouncer.debounce(this, block)
            debouncer.debounce(this, block)
            debouncer.debounce(this, block)
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
            debouncer.debounce(this, block)
            delay(200)
            debouncer.debounce(this, block)
            delay(200)
            debouncer.debounce(this, block)
            debouncer.job!!.join()
            count shouldEqual 3
        }
    }
}