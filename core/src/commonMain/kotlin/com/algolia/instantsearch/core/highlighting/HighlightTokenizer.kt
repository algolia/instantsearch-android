package com.algolia.instantsearch.core.highlighting


private const val DEFAULT_PRETAG: String = "<em>"
private const val DEFAULT_POSTTAG: String = "</em>"

public class HighlightTokenizer(
    public val preTag: String = DEFAULT_PRETAG,
    public val postTag: String = DEFAULT_POSTTAG
) {

    private val regex = Regex(Regex.escape(preTag) + ".*?" + Regex.escape(postTag))

    fun parse(input: String): HighlightedString {
        return HighlightedString(input, mutableListOf<HighlightToken>().apply {
            var startIndex = 0
            var matchResult = regex.find(input)

            while (matchResult != null && matchResult.groups.isNotEmpty()) {
                val group = matchResult.groups[0]!!
                val groupStart = input.indexOf(group.value, startIndex)
                val groupEnd = groupStart + group.value.length
                val textBeforeHighlight = input.substring(startIndex, groupStart)
                val textHighlighted = input.substring(groupStart + preTag.length, groupEnd - postTag.length)

                if (groupStart != startIndex) { // There was unmatched input before this match
                    add(HighlightToken(false, textBeforeHighlight))
                }
                add(HighlightToken(true, textHighlighted))

                startIndex = groupEnd
                matchResult = matchResult.next()
            }

            if (startIndex != input.length) { // Some input remains after the last match
                add(HighlightToken(false, input.substring(startIndex)))
            }
        })
    }
}