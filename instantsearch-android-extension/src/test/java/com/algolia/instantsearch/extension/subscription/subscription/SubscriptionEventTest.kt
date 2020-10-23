package com.algolia.instantsearch.extension.subscription.subscription

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.extension.subscription.MainCoroutineRule
import com.algolia.instantsearch.extension.subscription.asFlow
import com.algolia.instantsearch.extension.subscription.asLiveData
import com.algolia.instantsearch.extension.subscription.runBlocking
import com.algolia.instantsearch.extension.subscription.testCoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SubscriptionEventTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testAsFlow() {
        coroutineRule.runBlocking {
            val subscriptionEvent = SubscriptionEvent<Int>()
            val results = mutableListOf<Int>()
            testCoroutineScope {
                subscriptionEvent.asFlow()
                    .onEach { results.add(it) }
                    .launchIn(this)
                subscriptionEvent.send(1)
                subscriptionEvent.send(2)
            }
            subscriptionEvent.send(3)
            assertEquals(2, results.size)
        }
    }

    @Test
    fun testLiveData() {
        coroutineRule.runBlocking {
            val subscriptionEvent = SubscriptionEvent<Int>()
            val results = mutableListOf<Int>()
            val observer = Observer<Int> { results.add(it) }

            val livedata = subscriptionEvent.asLiveData()

            livedata.observeForever(observer)
            subscriptionEvent.send(1)
            subscriptionEvent.send(2)
            livedata.removeObserver(observer)
            subscriptionEvent.send(3)

            assertEquals(2, results.size)
        }
    }
}
