package connection

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.loading.LoadingView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import shouldBeTrue
import shouldEqual
import kotlin.test.Test

class TestConnection {

    private val viewModel = LoadingViewModel()
    private val connectionHandler = ConnectionHandler()
    private val view = object : LoadingView {
        override var onReload: Callback<Unit>? = null
        override fun setIsLoading(isLoading: Boolean) = Unit
    }

    @Test
    fun connectionHandlerHandlesDuplicates() {
        connectionHandler.connections.size shouldEqual 0
        connectionHandler += viewModel.connectView(view)
        connectionHandler.connections.size shouldEqual 1
        connectionHandler += viewModel.connectView(view)
        connectionHandler.connections.size shouldEqual 1
        connectionHandler.connections.all { it.isConnected }.shouldBeTrue()
        connectionHandler.disconnect()
        connectionHandler.connections.all { !it.isConnected }.shouldBeTrue()
    }

    @Test
    fun subscriptionHandlesDuplicates() {
        val connection = viewModel.connectView(view)

        viewModel.isLoading.subscriptions.size shouldEqual 0
        connection.connect()
        viewModel.isLoading.subscriptions.size shouldEqual 1
        connection.connect()
        viewModel.isLoading.subscriptions.size shouldEqual 1
        connection.disconnect()
        viewModel.isLoading.subscriptions.size shouldEqual 0
    }
}
