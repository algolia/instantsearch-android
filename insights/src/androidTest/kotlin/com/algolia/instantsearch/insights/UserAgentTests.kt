package com.algolia.instantsearch.insights

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.internal.webservice.computeUserAgent
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
internal class UserAgentTest {

    @Test
    fun testUserAgent() {
        val userAgent = computeUserAgent()
        val expectedUserAgent =
            "insights-android (${BuildConfig.INSIGHTS_VERSION}); Android (${Build.VERSION.SDK_INT}.0.0)"

        // Expected output: insights-android (LIB_VERSION); Android (ANDROID_VERSION)
        assertEquals(expectedUserAgent, userAgent)
    }
}
