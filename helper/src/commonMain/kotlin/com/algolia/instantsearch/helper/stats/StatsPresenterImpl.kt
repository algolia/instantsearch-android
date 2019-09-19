package com.algolia.instantsearch.helper.stats

import com.algolia.search.model.response.ResponseSearch
import kotlin.jvm.JvmField


public class StatsPresenterImpl(
    @JvmField private val default: String = ""
) : StatsPresenter<String> {

    override fun invoke(response: ResponseSearch?): String {
        return buildString {
            if (response != null) {
                val exhaustive = if (response.exhaustiveNbHitsOrNull == false) "~" else ""

                if (response.nbHitsOrNull != null) append("$exhaustive${response.nbHitsOrNull} hits")
                if (response.processingTimeMSOrNull != null) append(" in ${response.processingTimeMSOrNull}ms")
            } else default
        }
    }
}