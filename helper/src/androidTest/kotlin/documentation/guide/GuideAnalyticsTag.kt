package documentation.guide

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.analyticsTags
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Test


class GuideAnalyticsTag {

    private val index = ClientSearch(ApplicationID("app"), APIKey("key")).initIndex(IndexName("index"))

    @Suppress("UNUSED_VARIABLE")
    @Test
    fun snippet1() {
        val query = query {
            analyticsTags {
                +"visitingStatus"
            }
        }

        val searcher = SearcherSingleIndex(index, query)
    }

    fun getAgeGroup(birthYear: Int): String {
        return when {
            1996 <= birthYear -> "Generation Z"
            birthYear in 1977..1995 -> "Generation Y"
            else -> "Generation X or older"
        }
    }

    @Test
    fun snippet2() {
        val birthYear = 1975 // Fetch your user's birth year from your back end
        val ageGroup = getAgeGroup(birthYear)
    }

    fun getPlatform() = ""

    @Test
    fun snippet3() {
        val ageGroup = getAgeGroup(1975)
        val query = query {
            analyticsTags {
                +ageGroup
            }
        }

        val searcher = SearcherSingleIndex(index, query)
    }

    @Test
    fun snippet4() {
        val platform: String = getPlatform()
        val query = query {
            analyticsTags {
                +platform
            }
        }

        val searcher = SearcherSingleIndex(index, query)
    }
}