package searcher

import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.Index
import com.algolia.search.model.Attribute
import kotlinx.coroutines.Dispatchers

val TestCoroutineScope = SearcherScope(Dispatchers.Default)

fun TestSearcherSingle(index: Index) = SearcherSingleIndex(
    index = index,
    isDisjunctiveFacetingEnabled = false,
    coroutineScope = TestCoroutineScope
)

fun TestSearcherForFacets(index: Index, attribute: Attribute) = SearcherForFacets(
    index = index,
    attribute = attribute,
    coroutineScope = TestCoroutineScope
)

@OptIn(com.algolia.instantsearch.ExperimentalInstantSearch::class)
fun TestSearcherAnswers(index: Index) = SearcherAnswers(
    index = index,
    coroutineScope = TestCoroutineScope
)
