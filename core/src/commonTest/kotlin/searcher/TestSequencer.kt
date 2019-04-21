
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import searcher.Sequencer
import kotlin.random.Random
import kotlin.test.Test


class TestSequencer {

    @Test
    fun cancelFirst() {
        blocking {
            val sequencer = Sequencer(5)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.size shouldEqual sequencer.maxOperations
            sequencer.operations shouldEqual operations.subList(5, 10)
            sequencer.operations[0].cancelAndJoin()
            sequencer.operations shouldEqual operations.subList(6, 10)
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelLast() {
        blocking {
            val sequencer = Sequencer(5)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.size shouldEqual sequencer.maxOperations
            sequencer.operations shouldEqual operations.subList(5, 10)
            sequencer.operations[4].cancelAndJoin()
            sequencer.operations.shouldBeEmpty()
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelThird() {
        blocking {
            val sequencer = Sequencer(5)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.size shouldEqual sequencer.maxOperations
            sequencer.operations shouldEqual operations.subList(5, 10)
            sequencer.operations[2].cancelAndJoin()
            sequencer.operations shouldEqual operations.subList(8, 10)
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelAll() {
        blocking {
            val sequencer = Sequencer(5)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.cancelAll()
            sequencer.operations.shouldBeEmpty()
        }
    }

    @Test
    fun massRun() {
        blocking {
            val sequencer = Sequencer(5)

            val operations = (0..100000).map {
                launch { delay(Random.nextLong(50, 500)) }
            }

            operations.forEach(sequencer::addOperation)
            operations.joinAll()
            sequencer.operations.shouldBeEmpty()
        }
    }

    @Test
    fun arguments() {
        IllegalArgumentException::class shouldFailWith { Sequencer(0) }
        IllegalArgumentException::class shouldFailWith { Sequencer(-1) }
        Sequencer(1).maxOperations shouldEqual 1
    }
}