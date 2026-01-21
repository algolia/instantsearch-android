package searcher

import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.filters
import com.algolia.instantsearch.filter.state.groupAnd
import com.algolia.instantsearch.filter.state.toFilterGroups
import com.algolia.instantsearch.searcher.connectFilterState
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.client.model.search.SearchResponses
import mockClient
import respondJson
import shouldBeNull
import shouldEqual
import kotlin.test.Test

class TestSearcherMultipleIndex {

    private val client = mockClient(respondJson(SearchResponses(listOf()), SearchResponses.serializer()))
    private val filterState = FilterState(
        filters {
            group(groupAnd("group")) {
                facet("color", "red")
            }
        }
    )
    private val indexA = "indexA"
    private val indexB = "indexB"
    private val multiSearcher = MultiSearcher(client)
    private val hitsSearcherA = multiSearcher.addHitsSearcher(indexA)
    private val hitsSearcherB = multiSearcher.addHitsSearcher(indexB)

    @Test
    fun connectShouldUpdateProperQueryFilters() {
        hitsSearcherA.query.filters.shouldBeNull()
        hitsSearcherA.connectFilterState(filterState).connect()

        hitsSearcherA.filterGroups shouldEqual filterState.toFilterGroups()
        hitsSearcherB.filterGroups shouldEqual emptySet()
    }
}
