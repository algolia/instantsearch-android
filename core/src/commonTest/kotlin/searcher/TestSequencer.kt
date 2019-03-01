import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import searcher.Sequencer
import kotlin.test.Test


class TestSequencer {

    @Test
    fun sequencer() {
        blocking {
            val operations = (0 until 10).map { launch { delay(Long.MAX_VALUE) } }

            Sequencer(5).apply {
                operations.forEach(::addOperation)
                operations.size shouldEqual maxOperations
                operationCompleted(operations[8])
                operations.size shouldEqual 2
                cancelAll()
                operations.shouldBeEmpty()
            }
        }
    }

    @Test
    fun maxOperations() {
        IllegalArgumentException::class shouldFailWith { Sequencer(0) }
        IllegalArgumentException::class shouldFailWith { Sequencer(-1) }
        Sequencer(1).maxOperations shouldEqual 1
    }
}