package com.algolia.instantsearch.insights

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.util.setupWorkManager
import com.algolia.search.model.IndexName
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class InsightsAndroidTest {

    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val configuration = Insights.Configuration(
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )

    @Before
    fun init() {
        setupWorkManager()
    }

    @Test
    fun testSharedWithoutRegister() {
        try {
            val indexName = IndexName("index")
            Insights.shared(indexName)
        } catch (exception: Exception) {
            assertTrue(exception is InsightsException.IndexNotRegistered)
        }
    }

    @Test
    fun testSharedAfterRegister() {
        val indexName = IndexName("indexSharedAfter")
        val insights = Insights.register(context, "testApp", "testKey", indexName, configuration)
        val insightsShared = Insights.shared
        assertEquals(insights, insightsShared)
    }

    @Test
    fun testSharedNamedAfterRegister() {
        val indexName = IndexName("indexSharedNamedAfter")
        val insights = Insights.register(context, "testApp", "testKey", indexName, configuration)
        val insightsShared = Insights.shared(indexName)
        assertEquals(insights, insightsShared)
    }

    @Test
    fun testRegisterGlobalUserToken() {
        val indexName = IndexName("indexGlobalUserToken")
        Insights.register(context, "testApp", "testKey", indexName, configuration)
        val insightsShared = Insights.shared(indexName)
        assertEquals(configuration.defaultUserToken, insightsShared.userToken)
    }
}
