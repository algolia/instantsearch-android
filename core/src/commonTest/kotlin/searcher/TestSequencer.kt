
import kotlinx.atomicfu.AtomicArray
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import searcher.Sequencer
import kotlin.random.Random
import kotlin.test.Test


class TestSequencer {

    private val maxOperations = 5

    private fun AtomicArray<*>.shouldBeEmpty() {
        (0 until maxOperations).all { this[it].value == null }.shouldBeTrue()
    }

    private fun AtomicArray<*>.toList(): List<*> {
        return (0 until maxOperations).mapNotNull { this[it].value }
    }

    @Test
    fun cancelFirst() {
        blocking {
            val sequencer = Sequencer(maxOperations)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.toList() shouldEqual operations.subList(5, 10)
            sequencer.operations[0].value!!.cancelAndJoin()
            sequencer.operations.toList() shouldEqual operations.subList(6, 10)
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelLast() {
        blocking {
            val sequencer = Sequencer(maxOperations)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.toList() shouldEqual operations.subList(5, 10)
            sequencer.operations[4].value!!.cancelAndJoin()
            sequencer.operations.shouldBeEmpty()
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelThird() {
        blocking {
            val sequencer = Sequencer(maxOperations)
            val operations = (0 until 10).map {
                launch { delay(Long.MAX_VALUE) }
            }

            operations.forEach(sequencer::addOperation)
            sequencer.operations.toList() shouldEqual operations.subList(5, 10)
            sequencer.operations[2].value!!.cancelAndJoin()
            sequencer.operations.toList() shouldEqual operations.subList(8, 10)
            sequencer.cancelAll()
        }
    }

    @Test
    fun cancelAll() {
        blocking {
            val sequencer = Sequencer(maxOperations)
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
            val sequencer = Sequencer(maxOperations)

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

    @Test
    fun currentOperation() {
        blocking {
            val sequencer = Sequencer(1)
            val operationA = launch { delay(Long.MAX_VALUE) }
            val operationB = launch { delay(Long.MAX_VALUE) }

            sequencer.currentOperation shouldEqual null
            sequencer.addOperation(operationA)
            sequencer.currentOperation shouldEqual operationA
            sequencer.addOperation(operationB)
            sequencer.currentOperation shouldEqual operationB
            sequencer.cancelAll()
        }
    }
}