import com.algolia.instantsearch.core.searcher.Sequencer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.atomicfu.AtomicArray
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

class TestSequencer {

    private val maxOperations = 5

    private fun AtomicArray<*>.shouldBeEmpty() {
        (0 until maxOperations).all { this[it].value == null }.shouldBeTrue()
    }

    private fun AtomicArray<*>.toList(): List<*> {
        return (0 until maxOperations).mapNotNull { this[it].value }
    }

    @Test
    fun cancelFirst() = runTest {
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

    @Test
    fun cancelLast() = runTest {
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

    @Test
    fun cancelThird() = runTest {
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

    @Test
    fun cancelAll() = runTest {
        val sequencer = Sequencer(maxOperations)
        val operations = (0 until 10).map {
            launch { delay(Long.MAX_VALUE) }
        }

        operations.forEach(sequencer::addOperation)
        sequencer.cancelAll()
        sequencer.operations.shouldBeEmpty()
    }

    @Test
    fun massRun() = runTest {
        val sequencer = Sequencer(maxOperations)

        val operations = (0..1000).map {
            launch { delay(Random.nextLong(50, 500)) }
        }

        operations.forEach(sequencer::addOperation)
        operations.joinAll()
        sequencer.operations.shouldBeEmpty()
    }

    @Test
    fun arguments() = runTest {
        assertFailsWith(IllegalArgumentException::class) { Sequencer(0) }
        assertFailsWith(IllegalArgumentException::class) { Sequencer(-1) }
        Sequencer(1).maxOperations shouldEqual 1
    }

    @Test
    fun currentOperation() = runTest {
        val sequencer = Sequencer(1)
        val operationA = launch { delay(Long.MAX_VALUE) }
        val operationB = launch { delay(Long.MAX_VALUE) }

        sequencer.currentOperationOrNull shouldEqual null
        sequencer.addOperation(operationA)
        sequencer.currentOperation shouldEqual operationA
        sequencer.addOperation(operationB)
        sequencer.currentOperation shouldEqual operationB
        sequencer.cancelAll()
    }
}
