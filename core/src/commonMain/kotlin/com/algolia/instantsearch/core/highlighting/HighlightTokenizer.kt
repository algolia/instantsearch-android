package com.algolia.instantsearch.core.highlighting


private const val DefaultPreTag: String = "<em>"
private const val DefaultPostTag: String = "</em>"

public class HighlightTokenizer(
    public val preTag: String = DefaultPreTag,
    public val postTag: String = DefaultPostTag
) : ((String) -> HighlightedString) {

    private val regex = Regex(Regex.escape(preTag) + ".*?" + Regex.escape(postTag))

    override fun invoke(p1: String): HighlightedString = HighlightedString(p1, mutableListOf<HighlightToken>().apply {
        var startIndex = 0

        regex.findAll(p1).forEach {
            val groupStart = p1.indexOf(it.value, startIndex)
            val groupEnd = groupStart + it.value.length
            val textBeforeHighlight = p1.substring(startIndex, groupStart)
            val textHighlighted = p1.substring(groupStart + preTag.length, groupEnd - postTag.length)

            if (groupStart != startIndex) { // There was unmatched input before this match
                add(HighlightToken(textBeforeHighlight, false))
            }
            add(HighlightToken(textHighlighted, true))

            startIndex = groupEnd
        }

        if (startIndex != p1.length) { // Some input remains after the last match
            add(HighlightToken(p1.substring(startIndex), false))
        }
    })
}