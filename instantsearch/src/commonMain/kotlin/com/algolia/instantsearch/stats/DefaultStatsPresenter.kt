package com.algolia.instantsearch.stats

import com.algolia.client.model.search.SearchResponse

public class DefaultStatsPresenter(
    private val default: String = "",
) : StatsPresenter<String> {

    override fun invoke(response: SearchResponse?): String {
        return buildString {
            if (response != null) {
                val exhaustive = if (response.exhaustiveNbHits == false) "~" else ""

                if (response.nbHits != null) append("$exhaustive${response.nbHits} hits")
                if (response.processingTimeMS != null) append(" in ${response.processingTimeMS}ms")
            } else default
        }
    }
}

@Deprecated(message = "use DefaultStatsPresenter instead", replaceWith = ReplaceWith("DefaultStatsPresenter"))
public typealias StatsPresenterImpl = DefaultStatsPresenter
