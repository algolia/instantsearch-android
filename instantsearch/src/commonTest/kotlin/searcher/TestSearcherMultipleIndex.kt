package searcher

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResponseSearches
import kotlin.test.Test
import mockClient
import respondJson
import shouldBeNull
import shouldEqual

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
    private val multiSearcher = MultiSearcher(client)
    private val hitsSearcherA = multiSearcher.addHitsSearcher(indexA)
    private val hitsSearcherB = multiSearcher.addHitsSearcher(indexB)

    @Test
    fun connectShouldUpdateProperQueryFilters() {
        hitsSearcherA.query.filters.shouldBeNull()
        hitsSearcherA.connectFilterState(filterState).connect()

        hitsSearcherA.query.filters shouldEqual "(\"color\":\"red\")"
        hitsSearcherB.query.filters.shouldBeNull()
    }
}
