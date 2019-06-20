package highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import shouldBeEmpty
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestHighlightTokenizer {

    @Test
    fun parseMatch() {
        val parser = HighlightTokenizer()
        val input = "This <em>John</em> Doe looks like <em>John</em>athan."
        val parsed = parser.parse(input)

        parsed.original shouldEqual input
        parsed.parts.size shouldEqual 5
        parsed.highlightedParts shouldEqual listOf("John", "John")
    }

    @Test
    fun parseNoMatch() {
        val parser = HighlightTokenizer()
        val input = "This John Doe looks like Johnathan."
        val parsed = parser.parse(input)

        parsed.original shouldEqual input
        parsed.parts.size shouldEqual 1
        parsed.parts.any { it.highlighted }.shouldBeFalse()
        parsed.highlightedParts.shouldBeEmpty()
    }

    @Test
    fun parseOnlyMatch() {
        val parser = HighlightTokenizer()
        val input = "<em>John</em><em>John</em>"
        val parsed = parser.parse(input)

        parsed.original shouldEqual input
        println(parsed.parts)
        parsed.parts.size shouldEqual 2
        parsed.parts.all { it.highlighted }.shouldBeTrue()
        parsed.highlightedParts shouldEqual listOf("John", "John")
    }

    @Test
    fun parseCustomTags() {
        val parser = HighlightTokenizer("^", "$")
        val parsed = parser.parse("This ^Game$ is really ^funny$, don't you ^think$?")

        parsed.parts.size shouldEqual 7
        parsed.highlightedParts shouldEqual listOf("Game", "funny", "think")
    }
}