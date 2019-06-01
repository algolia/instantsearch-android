package com.algolia.instantsearch.helper.stats

import com.algolia.search.model.response.ResponseSearch


public class StatsPresenterImpl(
    private val default: String = ""
) : StatsPresenter<String> {

    override fun invoke(response: ResponseSearch?): String {
        return buildString {
            if (response != null) {
                if (response.nbHitsOrNull != null) append("${response.nbHitsOrNull} hits")
                if (response.processingTimeMSOrNull != null) append(" in ${response.processingTimeMSOrNull}ms")
            } else default
        }
    }
}