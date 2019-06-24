package highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestHighlightTokenizer {

    /*
    * matchStart
    * matchEnd
    * matchMiddle
    * */

    @Test
    fun matchNone() = shouldMatch(
        "This John Doe looks like Johnathan.",
        1
    )

    @Test
    fun matchAll() = shouldMatch(
        "<em>John</em><em>John</em>",
        2, listOf("John", "John")
    )

    @Test
    fun matchStart() = shouldMatch(
        "<em>John</em> is a nice guy.",
        2, listOf("John")
    )

    @Test
    fun matchEnd() = shouldMatch(
        "I love <em>John</em>",
        2, listOf("John")
    )

    @Test
    fun matchTwiceMiddle() = shouldMatch(
        "This <em>John</em> Doe looks like <em>John</em>athan.",
        5, listOf("John", "John")
    )

    @Test
    fun matchWithCustomTags() =
        shouldMatch(
            "This ^Game$ is really ^funny$, don't you ^think$?",
            7, listOf("Game", "funny", "think"),
            Pair("^", "$")
        )

    private fun shouldMatch(
        input: String,
        expectNbParts: Int = 1,
        expectHighlightedParts: List<String> = listOf(),
        tags: Pair<String, String>? = null
    ) {
        val tokenizer = if (tags != null) HighlightTokenizer(tags.first, tags.second) else HighlightTokenizer()
        val output = tokenizer(input)

        output.original shouldEqual input
        output.parts.size shouldEqual expectNbParts
        output.highlightedParts shouldEqual expectHighlightedParts

        if (expectHighlightedParts.isEmpty()) { // No part should be highlighted
            output.parts.any { it.highlighted }.shouldBeFalse()
        } else if (expectNbParts == expectHighlightedParts.size) { // All parts should be highlighted
            output.parts.all { it.highlighted }.shouldBeTrue()
        }
    }
}