package dx

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest

import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.connection.ConnectionImpl

import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class KotlinDX {

    @Test
    fun testConnection() {
        val handler = ConnectionHandler()
        handler += ConnectionImpl()
        handler.disconnect()
    }
}