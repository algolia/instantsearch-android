package customdata

import com.algolia.instantsearch.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.customdata.connectSearcher
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.client.model.search.SearchResponse
import com.algolia.search.model.IndexName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import mockClient
import org.junit.Test
import shouldEqual

internal class TestQueryRuleCustomData {

    @Serializable
    internal data class TestModel(val number: Int, val text: String)

    private val client = mockClient()

    @Test
    fun testSingleIndexSearcherConnection() {
        val searcher = HitsSearcher(client, "A")
        val viewModel = QueryRuleCustomDataViewModel(TestModel.serializer())
        viewModel.connectSearcher(searcher).connect()
        val customData = TestModel(number = 10, text = "test")

        val userData = Json.encodeToJsonElement(TestModel.serializer(), customData).jsonObject
        searcher.response.value = SearchResponse(
            hits = emptyList(),
            query = "",
            params = "",
            userData = userData
        )

        viewModel.item.value shouldEqual customData
    }

    @Test
    fun testFunctionBuildersSingleIndex() {
        val searcher = HitsSearcher(client, "A")
        val initialModel = TestModel(number = 10, text = "test1")

        // explicit
        QueryRuleCustomDataConnector(
            searcher = searcher,
            deserializer = TestModel.serializer(),
            initialItem = initialModel
        ) {
            it shouldEqual initialModel
        }

        // minimal
        QueryRuleCustomDataConnector<TestModel>(searcher = searcher) {
            it shouldEqual initialModel
        }

        // w/ initial
        QueryRuleCustomDataConnector(searcher = searcher, initialItem = initialModel) {
            it shouldEqual initialModel
        }

        // without initial
        QueryRuleCustomDataConnector<TestModel>(searcher = searcher) {
            it shouldEqual initialModel
        }

        // w/ ViewModel
        QueryRuleCustomDataConnector(
            searcher = searcher,
            viewModel = QueryRuleCustomDataViewModel(initialItem = initialModel)
        ) {
            it shouldEqual initialModel
        }
    }

    @Test
    fun testFunctionBuildersMultipleIndex() {
        val multiSearcher = MultiSearcher(client)
        val hitsSearcher = multiSearcher.addHitsSearcher("IndexMovie")
        val initialModel = TestModel(number = 10, text = "test1")

        // explicit
        QueryRuleCustomDataConnector(
            searcher = hitsSearcher,
            deserializer = TestModel.serializer(),
            initialItem = initialModel
        ) {
            it shouldEqual initialModel
        }

        // minimal
        QueryRuleCustomDataConnector<TestModel>(searcher = hitsSearcher) {
            it shouldEqual initialModel
        }

        // without initial
        QueryRuleCustomDataConnector<TestModel>(searcher = hitsSearcher) {
            it shouldEqual initialModel
        }

        // w/ ViewModel
        QueryRuleCustomDataConnector(
            searcher = hitsSearcher,
            viewModel = QueryRuleCustomDataViewModel(initialItem = initialModel)
        ) {
            it shouldEqual initialModel
        }
    }
}
