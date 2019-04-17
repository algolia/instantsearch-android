import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import searcher.Sequencer
import kotlin.random.Random
import kotlin.test.Test


class TestSequencer {

    @Test
    fun sequencer() {
        blocking {
            val sequencer = Sequencer(5)
            val operations = (0 until 10).map {
                val job = launch { delay(Long.MAX_VALUE) }

                sequencer.addOperation(job)
                job.invokeOnCompletion { sequencer.operationCompleted(job) }
                job
            }

            sequencer.operations.size shouldEqual sequencer.maxOperations
            sequencer.operationCompleted(operations[8])
            sequencer.operations.size shouldEqual 2
            sequencer.cancelAll()
            sequencer.operations.shouldBeEmpty()
        }
    }

    @Test
    fun massRun() {
        blocking {
            val sequencer = Sequencer(5)

            for (index in 0..100000) {
                val job = launch { delay(Random.nextLong(500, 5000)) }

                sequencer.addOperation(job)
                job.invokeOnCompletion { sequencer.operationCompleted(job) }
            }
        }
    }

    @Test
    fun arguments() {
        IllegalArgumentException::class shouldFailWith { Sequencer(0) }
        IllegalArgumentException::class shouldFailWith { Sequencer(-1) }
        Sequencer(1).maxOperations shouldEqual 1
    }
}