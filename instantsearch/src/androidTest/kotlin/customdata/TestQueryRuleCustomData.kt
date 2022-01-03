package customdata

import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.helper.customdata.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
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
        val searcher = HitsSearcher(client, IndexName("A"))
        val viewModel = QueryRuleCustomDataViewModel(TestModel.serializer())
        viewModel.connectSearcher(searcher).connect()
        val customData = TestModel(number = 10, text = "test")

        val userData = Json.encodeToJsonElement(TestModel.serializer(), customData).jsonObject
        searcher.response.value = ResponseSearch(userDataOrNull = listOf(userData))

        viewModel.item.value shouldEqual customData
    }

    @Test
    fun testFunctionBuildersSingleIndex() {
        val searcher = HitsSearcher(client, IndexName("A"))
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
        val indexA = IndexQuery(IndexName("IndexMovie"))
        val indexB = IndexQuery(IndexName("IndexActor"))

        @Suppress("DEPRECATION")
        val searcher = SearcherMultipleIndex(client, listOf(indexA, indexB))
        val queryIndex = 1
        val initialModel = TestModel(number = 10, text = "test1")

        // explicit
        QueryRuleCustomDataConnector(
            searcher = searcher,
            queryIndex = queryIndex,
            deserializer = TestModel.serializer(),
            initialItem = initialModel
        ) {
            it shouldEqual initialModel
        }

        // minimal
        QueryRuleCustomDataConnector<TestModel>(searcher = searcher, queryIndex = queryIndex) {
            it shouldEqual initialModel
        }

        // w/ initial
        QueryRuleCustomDataConnector(searcher = searcher, queryIndex = queryIndex, initialItem = initialModel) {
            it shouldEqual initialModel
        }

        // without initial
        QueryRuleCustomDataConnector<TestModel>(searcher = searcher, queryIndex = queryIndex) {
            it shouldEqual initialModel
        }

        // w/ ViewModel
        QueryRuleCustomDataConnector(
            searcher = searcher,
            queryIndex = queryIndex,
            viewModel = QueryRuleCustomDataViewModel(initialItem = initialModel)
        ) {
            it shouldEqual initialModel
        }
    }
}
