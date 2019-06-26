package highlighting

import com.algolia.instantsearch.helper.highlighting.Highlighter
import com.algolia.instantsearch.helper.highlighting.toHighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.model.search.MatchLevel
import com.algolia.search.serialize.Key_HighlightResult
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.json
import kotlinx.serialization.list
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestHighlighter {

    private val preTag = "["
    private val postTag = "]"
    private val name = Attribute("name")
    private val nameHighlight = HighlightResult("[John]ny", MatchLevel.Partial, listOf("Johnny"))
    private val nickname = Attribute("nickname")
    private val nicknameHighlight = listOf(
        HighlightResult("Little [John]", MatchLevel.Partial, listOf("Johnny")),
        HighlightResult("[John] the wise", MatchLevel.Partial, listOf("John"))
    )
    private val city = Attribute("city")
    private val cityHighlight = HighlightResult("[John]son Town", MatchLevel.Partial, listOf("Johnson"))

    private val hit = ResponseSearch.Hit(json {
        name.raw to "Johnny"
        "age" to 42
        Key_HighlightResult to Json(JsonConfiguration.Stable).toJson(
            HashMapSerializer(
                Attribute,
                HighlightResult.serializer()
            ), mapOf(name to nameHighlight, city to cityHighlight)
        )
    })

    //FIXME: Can I serialize a Highlight with both flat and list highlights?
    private val hitWithList = ResponseSearch.Hit(json {
        name.raw to "Johnny"
        "age" to 42
        Key_HighlightResult to Json(JsonConfiguration.Stable).toJson(
            HashMapSerializer(
                Attribute,
                HighlightResult.serializer().list
            ), mapOf(nickname to nicknameHighlight)
        )
    })

    @Test
    fun toHighlightedString() {
        val highlight = hit.toHighlightedString(name, preTag, postTag)

        highlight.shouldNotBeNull()
        highlight?.let {
            it.parts.size shouldEqual 2
            it.highlightedParts shouldEqual listOf("John")
        }
    }

    @Test
    fun getHighlights() {
        val highlights = Highlighter.getHighlights(hit.highlightResult, preTag, postTag)

        highlights.shouldNotBeNull()
        highlights?.let {
            it.size shouldEqual 2
            it.forEach { (_, list) ->
                list.forEach { value ->
                    value.parts.size shouldEqual 2
                    value.highlightedParts shouldEqual listOf("John")
                }
            }
        }
    }

    @Test
    fun getHighlightsWithList() {
        val highlights = Highlighter.getHighlights(hitWithList.highlightResult, preTag, postTag)

        highlights.shouldNotBeNull()
        highlights?.let {
            it.forEach { (_, list) ->
                list.size shouldEqual 2
                list.forEach { value ->
                    value.parts.size shouldEqual 2
                    value.highlightedParts shouldEqual listOf("John")
                }
            }
        }
    }
}