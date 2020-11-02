package customdata

import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataViewModel
import com.algolia.instantsearch.helper.customdata.connectSearcher
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearches
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import mockClient
import org.junit.Test
import shouldEqual

class TestQueryRuleCustomData {

    @Serializable
    internal data class TestModel(val number: Int, val text: String)

    private val client = mockClient()

    @Test
    fun testSingleIndexSearcherConnection() {
        val index = client.initIndex(IndexName("A"))
        val searcher = SearcherSingleIndex(index)
        val viewModel = QueryRuleCustomDataViewModel(TestModel.serializer())
        viewModel.connectSearcher(searcher).connect()
        val customData = TestModel(number = 10, text = "test")

        val userData = Json.encodeToJsonElement(TestModel.serializer(), customData).jsonObject
        searcher.response.value = ResponseSearch(userDataOrNull = listOf(userData))

        viewModel.item.value shouldEqual customData
    }

    @Test
    fun testMultiIndexSearcherConnection() {
        val indexA = IndexQuery(IndexName("IndexMovie"))
        val indexB = IndexQuery(IndexName("IndexActor"))
        val searcher = SearcherMultipleIndex(client, listOf(indexA, indexB))

        val viewModel = QueryRuleCustomDataViewModel(TestModel.serializer())
        viewModel.connectSearcher(searcher, 1).connect()
        val customData1 = TestModel(number = 10, text = "test1")
        val customData2 = TestModel(number = 20, text = "test2")

        val userData1 = Json.encodeToJsonElement(TestModel.serializer(), customData1).jsonObject
        val userData2 = Json.encodeToJsonElement(TestModel.serializer(), customData2).jsonObject

        searcher.response.value = ResponseSearches(
            listOf(
                ResponseSearch(userDataOrNull = listOf(userData1)),
                ResponseSearch(userDataOrNull = listOf(userData2))
            )
        )

        viewModel.item.value shouldEqual customData2
    }

    @Test
    fun testFunctionBuildersSingleIndex() {
        val index = client.initIndex(IndexName("A"))
        val searcher = SearcherSingleIndex(index)
        val initialModel = TestModel(number = 10, text = "test1")

        // explicit
        QueryRuleCustomDataConnector(searcher = searcher,
            deserializer = TestModel.serializer(),
            initialItem = initialModel) {
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
        QueryRuleCustomDataConnector(searcher = searcher,
            viewModel = QueryRuleCustomDataViewModel(initialItem = initialModel)) {
            it shouldEqual initialModel
        }
    }

    @Test
    fun testFunctionBuildersMultipleIndex() {
        val indexA = IndexQuery(IndexName("IndexMovie"))
        val indexB = IndexQuery(IndexName("IndexActor"))
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
