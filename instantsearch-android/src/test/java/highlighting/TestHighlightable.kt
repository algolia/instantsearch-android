package highlighting

import JsonNoDefaults
import com.algolia.instantsearch.core.highlighting.HighlightToken
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.model.search.MatchLevel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
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
        override val _highlightResult: JsonObject?,
    ) : Highlightable

    private val friend = Friend(
        name = "toto",
        age = 42,
        friendNames = listOf("foo", "bar"),
        pet = Pet("fido", listOf("fifi, dodo")),
        _highlightResult = buildJsonObject {
            put("name", HighlightResult("<em>to</em>to").toJson())
            put(
                "friendNames",
                buildJsonArray {
                    add(HighlightResult("<em>f</em>oo").toJson())
                    add(HighlightResult("b<em>a</em>r").toJson())
                }
            )
            put("age", HighlightResult("<em>4</em>2").toJson())
            put(
                "pet",
                buildJsonObject {
                    put("name", HighlightResult("fi<em>do</em>").toJson())
                    put(
                        "nicknames",
                        buildJsonArray {
                            add(HighlightResult("<em>fifi</em>").toJson())
                            add(HighlightResult("dodo").toJson())
                        }
                    )
                }
            )
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
        val highlightPetName = friend.getHighlight("name", { it.getValue("pet").jsonObject })!!

        highlightPetName.original shouldEqual "fi<em>do</em>"
        highlightPetName.tokens shouldEqual listOf(
            HighlightToken("fi", false),
            HighlightToken("do", true)
        )
    }

    @Test
    fun getHighlightObjectArray() {
        val highlightPetNicknames = friend.getHighlights("nicknames", { it.getValue("pet").jsonObject })!!

        highlightPetNicknames[0].let {
            it.original shouldEqual "<em>fifi</em>"
            it.tokens shouldEqual listOf(HighlightToken("fifi", true))
        }
        highlightPetNicknames[1].let {
            it.original shouldEqual "dodo"
            it.tokens shouldEqual listOf(HighlightToken("dodo", false))
        }
    }

    private operator fun HighlightResult.Companion.invoke(value: String): HighlightResult {
        return HighlightResult(
            value = value,
            matchLevel = MatchLevel.Full,
            matchedWords = listOf(),
            fullyHighlighted = false
        )
    }

    private fun HighlightResult.toJson(): JsonElement =
        JsonNoDefaults.encodeToJsonElement(HighlightResult.serializer(), this)
}
