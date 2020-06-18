package com.algolia.instantsearch.insights

import android.os.Build
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.evernote.android.job.JobManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class InsightsAndroidTest {

    private val context get() = InstrumentationRegistry.getContext()
    private val configuration = Insights.Configuration(
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )

    @Before
    fun init() {
        val declaredField = JobManager::class.java.getDeclaredField("instance")
        declaredField.isAccessible = true
        declaredField.set(null, null)
    }

    @Test
    fun testSharedWithoutRegister() {
        try {
            Insights.shared("index")
        } catch (exception: Exception) {
            assertTrue(exception is InsightsException.IndexNotRegistered)
        }
    }

    @Test
    fun testSharedAfterRegister() {
        val insights = Insights.register(context, "testApp", "testKey", "indexSharedAfter", configuration)
        val insightsShared = Insights.shared
        assertEquals(insights, insightsShared)
    }

    @Test
    fun testSharedNamedAfterRegister() {
        val insights = Insights.register(context, "testApp", "testKey", "indexSharedNamedAfter", configuration)
        val insightsShared = Insights.shared("indexSharedNamedAfter")
        assertEquals(insights, insightsShared)
    }

    @Test
    fun testRegisterGlobalUserToken() {
        Insights.register(context, "testApp", "testKey", "indexGlobalUserToken", configuration)
        val insightsShared = Insights.shared("indexGlobalUserToken")
        assertEquals(configuration.defaultUserToken, insightsShared.userToken)
    }
}
