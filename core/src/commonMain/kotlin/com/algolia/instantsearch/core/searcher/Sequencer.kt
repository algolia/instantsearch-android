package com.algolia.instantsearch.core.searcher

import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.coroutines.Job
import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

/**
 * A component that can sequence the operations it receives,
 * ensuring a [maximum number of concurrent operations][maxOperations]
 * by canceling its oldest job when a new one arrives.
 */
public class Sequencer(

    /**
     * How many concurrent operations are allowed.
     */
    @JvmField
    public val maxOperations: Int = 5
) {

    init {
        require(maxOperations > 0) { "Sequencer maxOperations should be higher than 0." }
    }

    internal val operations = atomicArrayOfNulls<Job>(maxOperations)
        @JvmSynthetic get

    public val currentOperationOrNull: Job?
        @JvmSynthetic get() {
            val index = (0 until maxOperations).findLast { operations[it].value != null }

            return index?.let { operations[it].value }
        }

    public val currentOperation: Job get() = currentOperationOrNull!!

    /**
     * When an operation is added, and the maxOperations count is reached, the oldest job in the queue is canceled
     * and removed from the queue.
     */
    public fun addOperation(operation: Job) {
        val index = (0 until maxOperations).find { operations[it].value == null }

        if (index != null) {
            operations[index].compareAndSet(null, operation)
        } else {
            repeat(maxOperations) {
                if (it == 0) operations[it].value!!.cancel()

                operations[it].value =
                    if (it == maxOperations - 1) operation else operations[it + 1].value
            }
        }
        operation.invokeOnCompletion { operationCompleted(operation) }
    }

    /**
     * When an operation completes, cancel and remove operations from the queue that are older.
     */
    private fun operationCompleted(operation: Job) {
        val index = (0 until maxOperations).find { operations[it].value == operation }

        if (index != null) {
            repeat(maxOperations) {
                val afterIndex = index + it + 1

                if (it < index) operations[it].value!!.cancel()
                operations[it].value =
                    if (afterIndex < maxOperations) operations[afterIndex].value else null
            }
        }
    }

    /**
     * Cancel and clear all operations from the queue.
     */
    public fun cancelAll() {
        repeat(maxOperations) {
            operations[it].getAndSet(null)?.cancel()
        }
    }
}
