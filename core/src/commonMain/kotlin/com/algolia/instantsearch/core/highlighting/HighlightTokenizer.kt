package com.algolia.instantsearch.core.highlighting

import kotlin.jvm.JvmOverloads

/**
 * HighlightTokenizer transform Algolia's `_highlightResult` into structured data easy to use
 * for building your UI. It can be customized to arbitrary pre/post tags.
 */
public class HighlightTokenizer @JvmOverloads constructor(
    public val preTag: String = DefaultPreTag,
    public val postTag: String = DefaultPostTag
) : (String) -> HighlightedString {

    private val regex = Regex(Regex.escape(preTag) + ".*?" + Regex.escape(postTag))


    /**
     * Tokenizes the input into a HighlightedString.
     *
     * @param p1 a String to parse.
     * @return a tokenized [HighlightedString].
     */
    override fun invoke(p1: String): HighlightedString = tokenize(p1)


    /**
     * Tokenizes the input into a HighlightedString.
     *
     * @param input a String to parse.
     * @return a tokenized [HighlightedString].
     */
    public fun tokenize(input: String): HighlightedString =
        HighlightedString(input, mutableListOf<HighlightToken>().apply {
            var startIndex = 0

            regex.findAll(input).forEach {
                val groupStart = input.indexOf(it.value, startIndex)
                val groupEnd = groupStart + it.value.length
                val textBeforeHighlight = input.substring(startIndex, groupStart)
                val textHighlighted =
                    input.substring(groupStart + preTag.length, groupEnd - postTag.length)

                if (groupStart != startIndex) { // There was unmatched input before this match
                    add(HighlightToken(textBeforeHighlight, false))
                }
                add(HighlightToken(textHighlighted, true))

                startIndex = groupEnd
            }

            if (startIndex != input.length) { // Some input remains after the last match
                add(HighlightToken(input.substring(startIndex), false))
            }
        })
}
