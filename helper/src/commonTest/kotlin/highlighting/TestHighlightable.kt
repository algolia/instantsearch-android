package highlighting

import com.algolia.instantsearch.core.highlighting.HighlightToken
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.serialize.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import shouldEqual
import kotlin.test.Test


class TestHighlightable {

    @Serializable
    private data class Pet(val name: String, val nickName: List<String>)

    @Serializable
    private data class Friend(
        val name: String,
        val age: Int,
        val friendNames: List<String>,
        val pet: Pet,
        override val _highlightResult: JsonObject?
    ) : Highlightable

    private val friend = Friend(
        "toto",
        42,
        listOf("foo", "bar"),
        Pet("fido", listOf("fifi, dodo")),
        json {
            "name" to jsonHighlight("<em>to</em>to")
            "friendNames" to jsonArray {
                +jsonHighlight("<em>f</em>oo")
                +jsonHighlight("b<em>a</em>r")
            }
            "age" to jsonHighlight("<em>4</em>2")
            "pet" to json {
                "name" to jsonHighlight("fi<em>do</em>")
                "nicknames" to jsonArray {
                    +jsonHighlight("<em>fifi</em>")
                    +jsonHighlight("dodo")
                }
            }
        }
    )

    @Test
    fun getHighlightString() {
        val highlightName = friend.getHighlight("name")!!

        highlightName.original shouldEqual "<em>to</em>to"
        highlightName.tokens shouldEqual listOf(
            HighlightToken("to", true),
            HighlightToken("to", false)
        )
    }

    @Test
    fun getHighlightInteger() {
        val highlightAge = friend.getHighlight("age")!!

        highlightAge.original shouldEqual "<em>4</em>2"
        highlightAge.tokens shouldEqual listOf(
            HighlightToken("4", true),
            HighlightToken("2", false)
        )
    }

    @Test
    fun getHighlightArray() {
        val highlightFriends = friend.getHighlights("friendNames")!!

        highlightFriends[0].let {
            it.original shouldEqual "<em>f</em>oo"
            it.tokens shouldEqual listOf(
                HighlightToken("f", true),
                HighlightToken("oo", false)
            )
        }
        highlightFriends[1].let {
            it.original shouldEqual "b<em>a</em>r"
            it.tokens shouldEqual listOf(
                HighlightToken("b", false),
                HighlightToken("a", true),
                HighlightToken("r", false)
            )
        }
    }

    @Test
    fun getHighlightObjectString() {
        val highlightPetName = friend.getHighlight("name", { it.getObject("pet") })!!

        highlightPetName.original shouldEqual "fi<em>do</em>"
        highlightPetName.tokens shouldEqual listOf(
            HighlightToken("fi", false),
            HighlightToken("do", true)
        )
    }

    @Test
    fun getHighlightObjectArray() {
        val highlightPetNicknames = friend.getHighlights("nicknames", { it.getObject("pet") })!!

        highlightPetNicknames[0].let {
            it.original shouldEqual "<em>fifi</em>"
            it.tokens shouldEqual listOf(HighlightToken("fifi", true))
        }
        highlightPetNicknames[1].let {
            it.original shouldEqual "dodo"
            it.tokens shouldEqual listOf(HighlightToken("dodo", false))
        }
    }

    private fun jsonHighlight(
        value: String,
        matchLevel: String = KeyFull,
        fullyHighlighted: Boolean = false,
        matchedWords: JsonArray = jsonArray { +value }
    ): JsonObject {
        return json {
            KeyValue to value
            KeyMatchLevel to matchLevel
            KeyFullyHighlighted to fullyHighlighted
            KeyMatchedWords to matchedWords
        }
    }
}