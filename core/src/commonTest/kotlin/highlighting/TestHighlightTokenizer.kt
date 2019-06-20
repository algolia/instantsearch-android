package highlighting

import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import shouldBeEmpty
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestHighlightTokenizer {

    @Test
    fun match() {
        val tokenizer = HighlightTokenizer()
        val input = "This <em>John</em> Doe looks like <em>John</em>athan."
        val output = tokenizer.parse(input)

        output.original shouldEqual input
        output.parts.size shouldEqual 5
        output.highlightedParts shouldEqual listOf("John", "John")
    }

    @Test
    fun noMatch() {
        val tokenizer = HighlightTokenizer()
        val input = "This John Doe looks like Johnathan."
        val output = tokenizer.parse(input)

        output.original shouldEqual input
        output.parts.size shouldEqual 1
        output.parts.any { it.highlighted }.shouldBeFalse()
        output.highlightedParts.shouldBeEmpty()
    }

    @Test
    fun onlyMatch() {
        val tokenizer = HighlightTokenizer()
        val input = "<em>John</em><em>John</em>"
        val output = tokenizer.parse(input)

        output.original shouldEqual input
        println(output.parts)
        output.parts.size shouldEqual 2
        output.parts.all { it.highlighted }.shouldBeTrue()
        output.highlightedParts shouldEqual listOf("John", "John")
    }

    @Test
    fun customTags() {
        val tokenizer = HighlightTokenizer("^", "$")
        val output = tokenizer.parse("This ^Game$ is really ^funny$, don't you ^think$?")

        output.parts.size shouldEqual 7
        output.highlightedParts shouldEqual listOf("Game", "funny", "think")
    }
}