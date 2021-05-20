package searcher

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.response.ResponseSearches
import mockClient
import respondJson
import shouldBeNull
import shouldEqual
import shouldFailWith
import kotlin.test.Test

class TestSearcherMultipleIndex {

    private val client = mockClient(respondJson(ResponseSearches(listOf()), ResponseSearches.serializer()))
    private val filterState = FilterState(
        filters {
            group(groupAnd("group")) {
                facet("color", "red")
            }
        }
    )
    private val indexA = IndexName("indexA")
    private val indexB = IndexName("indexB")
    private val indexC = IndexName("indexC")
    private val queries = listOf(
        IndexQuery(indexA),
        IndexQuery(indexB)
    )
    private val searcher = SearcherMultipleIndex(client, queries)

    @Test
    fun connectShouldUpdateProperQueryFilters() {
        val index = searcher.queries[0]

        index.query.filters.shouldBeNull()
        searcher.connectFilterState(filterState, indexA).connect()
        index.query.filters shouldEqual "(\"color\":\"red\")"
        searcher.queries[1].query.filters.shouldBeNull()
    }

    @Test
    fun connectWithWrongIndexShouldThrowException() {
        IllegalArgumentException::class.shouldFailWith {
            searcher.connectFilterState(filterState, indexC)
        }
    }
}
