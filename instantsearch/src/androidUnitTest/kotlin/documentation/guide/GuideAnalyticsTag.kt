package documentation.guide

import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.analyticsTags
import com.algolia.search.dsl.query
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import org.junit.Ignore
import org.junit.Test

@Suppress("UNUSED_VARIABLE")
@Ignore
internal class GuideAnalyticsTag {

    private val client = ClientSearch("app", "key")

    @Suppress("UNUSED_VARIABLE")
    @Test
    fun snippet1() {
        val query = query {
            analyticsTags {
                +"visitingStatus"
            }
        }

        val searcher = HitsSearcher(client, "YourIndexName", query)
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

        val searcher = HitsSearcher(client, "YourIndexName", query)
    }

    @Test
    fun snippet4() {
        val platform: String = getPlatform()
        val query = query {
            analyticsTags {
                +platform
            }
        }

        val searcher = HitsSearcher(client, "YourIndexName", query)
    }
}
