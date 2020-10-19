package com.algolia.instantsearch.extension.subscription.subscription

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.subscription.MainCoroutineRule
import com.algolia.instantsearch.extension.subscription.asFlow
import com.algolia.instantsearch.extension.subscription.asLiveData
import com.algolia.instantsearch.extension.subscription.runBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SubscriptionValueTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testAsFlow() {
        coroutineRule.runBlocking {
            val initialValue = 1 // should be ignored
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            coroutineScope {
                val job = subscriptionValue.asFlow()
                    .onEach { results.add(it) }
                    .launchIn(this)
                subscriptionValue.value = 2 // first element
                job.cancel()
            }
            assertEquals(1, results.size)
        }
    }

    @Test
    fun testAsFlowPast() {
        coroutineRule.runBlocking {
            val initialValue = 1 // first (current) element
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            coroutineScope {
                val job = subscriptionValue.asFlow(past = true)
                    .onEach { results.add(it) }
                    .launchIn(this)
                subscriptionValue.value = 2 // second element
                job.cancel()
            }
            assertEquals(2, results.size)
        }
    }

    @Test
    fun testAsFlowShouldFail() {
        coroutineRule.runBlocking {
            val initialValue = 1 // should be ignored
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            coroutineScope {
                val job = subscriptionValue.asFlow()
                    .onEach { results.add(it) }
                    .launchIn(this)
                job.cancel()
            }
            assertNotEquals(1, results.size)
        }
    }

    @Test
    fun testLiveData() {
        coroutineRule.runBlocking {
            val initialValue = 1 // should be ignored
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            val observer = Observer<Int> { results.add(it) }
            val livedata = subscriptionValue.asLiveData()

            livedata.observeForever(observer)
            subscriptionValue.value = 2 // first element
            livedata.removeObserver(observer)
            subscriptionValue.value = 3 // should be ignored

            assertEquals(1, results.size)
        }
    }

    @Test
    fun testLiveDataPast() {
        coroutineRule.runBlocking {
            val initialValue = 1 // first (current) element
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            val observer = Observer<Int> { results.add(it) }
            val livedata = subscriptionValue.asLiveData(past = true)

            livedata.observeForever(observer)
            subscriptionValue.value = 2 // second element
            livedata.removeObserver(observer)
            subscriptionValue.value = 3 // should be ignored

            assertEquals(2, results.size)
        }
    }

    @Test
    fun testLiveDataPastShouldFail() {
        coroutineRule.runBlocking {
            val initialValue = 1 // first (current) element
            val subscriptionValue = SubscriptionValue(initialValue)
            val results = mutableListOf<Int>()
            val observer = Observer<Int> { results.add(it) }
            val livedata = subscriptionValue.asLiveData()

            livedata.observeForever(observer)
            livedata.removeObserver(observer)
            subscriptionValue.value = 2 // should be ignored

            assertNotEquals(1, results.size)
        }
    }
}
