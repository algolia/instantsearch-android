package com.algolia.instantsearch.core.highlighting


private const val DefaultPreTag: String = "<em>"
private const val DefaultPostTag: String = "</em>"

public class HighlightTokenizer(
    public val preTag: String = DefaultPreTag,
    public val postTag: String = DefaultPostTag
) {

    private val regex = Regex(Regex.escape(preTag) + ".*?" + Regex.escape(postTag))

    fun tokenize(input: String): HighlightedString {
        return HighlightedString(input, mutableListOf<HighlightToken>().apply {
            var startIndex = 0
            var matchResult = regex.find(input)

            while (matchResult != null && matchResult.groups.isNotEmpty()) {
                matchResult.groups[0]?.let {
                    val groupStart = input.indexOf(it.value, startIndex)
                    val groupEnd = groupStart + it.value.length
                    val textBeforeHighlight = input.substring(startIndex, groupStart)
                    val textHighlighted = input.substring(groupStart + preTag.length, groupEnd - postTag.length)

                    if (groupStart != startIndex) { // There was unmatched input before this match
                        add(HighlightToken(textBeforeHighlight, false))
                    }
                    add(HighlightToken(textHighlighted, true))

                    startIndex = groupEnd
                }

                matchResult = matchResult.next()
            }

            if (startIndex != input.length) { // Some input remains after the last match
                add(HighlightToken(input.substring(startIndex), false))
            }
        })
    }
}