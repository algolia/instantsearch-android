package com.algolia.instantsearch.migration2to3

internal fun String.escape() = "\"${this.escapeQuotation()}\""
internal fun String.escapeQuotation() = this.replace("\"", "\\\"")

internal fun Filter.Facet.Value.toSQL(): String {
    return when (this) {
        is Filter.Facet.Value.String -> raw.escape()
        is Filter.Facet.Value.Number -> raw.toString()
        is Filter.Facet.Value.Boolean -> raw.toString()
    }
}

internal fun Filter.Facet.toSQL(): String {
    val value = value.toSQL()
    val attribute = attribute.escape()
    val score = if (score != null) "<score=$score>" else ""
    val expression = "$attribute:$value$score"

    return if (isNegated) "NOT $expression" else expression
}

internal fun Filter.Tag.toSQL(): String {
    val expression = "$attribute:${value.escape()}"

    return if (isNegated) "NOT $expression" else expression
}

internal fun Filter.Numeric.Value.Comparison.toSQL(attribute: Attribute, isNegated: Boolean): String {
    val expression = "${attribute.escape()} ${operator.raw} $number"

    return if (isNegated) "NOT $expression" else expression
}

internal fun Filter.Numeric.Value.Range.toSQL(attribute: Attribute, isNegated: Boolean): String {
    val expression = "${attribute.escape()}:$lowerBound TO $upperBound"

    return if (isNegated) "NOT $expression" else expression
}

internal fun Filter.Numeric.toSQL(): String {
    return when (value) {
        is Filter.Numeric.Value.Comparison -> value.toSQL(attribute, isNegated)
        is Filter.Numeric.Value.Range -> value.toSQL(attribute, isNegated)
    }
}

internal fun Filter.Numeric.Value.Comparison.toLegacy(
    attribute: Attribute,
    isNegated: Boolean,
    escape: Boolean
): List<String> {
    val operator = if (isNegated) {
        when (operator) {
            NumericOperator.Less -> NumericOperator.GreaterOrEquals
            NumericOperator.LessOrEquals -> NumericOperator.Greater
            NumericOperator.Equals -> NumericOperator.NotEquals
            NumericOperator.NotEquals -> NumericOperator.Equals
            NumericOperator.Greater -> NumericOperator.LessOrEquals
            NumericOperator.GreaterOrEquals -> NumericOperator.Less
        }
    } else operator

    val stringAttribute = if (escape) attribute.escape() else attribute
    return listOf("$stringAttribute ${operator.raw} $number")
}

internal fun Filter.Numeric.Value.Range.toLegacy(
    attribute: Attribute,
    isNegated: Boolean,
    escape: Boolean
): List<String> {
    val stringAttribute: String = if (escape) attribute.escape() else attribute

    return if (isNegated) {
        listOf("$stringAttribute < $lowerBound", "$stringAttribute > $upperBound")
    } else {
        listOf("$stringAttribute >= $lowerBound", "$stringAttribute <= $upperBound")
    }
}

internal fun Filter.Numeric.toLegacy(escape: Boolean): List<String> {
    return when (value) {
        is Filter.Numeric.Value.Range -> value.toLegacy(attribute, isNegated, escape)
        is Filter.Numeric.Value.Comparison -> value.toLegacy(attribute, isNegated, escape)
    }
}

internal fun Filter.Facet.Value.toLegacy(isNegated: Boolean, escape: Boolean): String {
    val value = when (this) {
        is Filter.Facet.Value.String -> if (escape) raw.escape() else raw.escapeQuotation()
        is Filter.Facet.Value.Number -> raw.toString()
        is Filter.Facet.Value.Boolean -> raw.toString()
    }
    return if (isNegated) "-$value" else value
}

internal fun Filter.Facet.toLegacy(escape: Boolean): List<String> {
    val value = value.toLegacy(isNegated, escape)
    val attribute = if (escape) attribute.escape() else attribute
    val score = if (score != null) "<score=$score>" else ""

    return listOf("$attribute:$value$score")
}

internal fun Filter.Tag.toLegacy(escape: Boolean): List<String> {
    val raw = if (escape) value.escape() else value.escapeQuotation()
    val value = if (isNegated) "-$raw" else raw

    return listOf(value)
}


public sealed class FilterConverter<I : Filter, O> : Converter<I, O> {

    /**
     * Converts a [Filter] to its SQL-like [String] representation.
     */
    public object SQL : FilterConverter<Filter, String>() {

        override fun invoke(input: Filter): String {
            return when (input) {
                is Filter.Facet -> input.toSQL()
                is Filter.Tag -> input.toSQL()
                is Filter.Numeric -> input.toSQL()
            }
        }
    }

    /**
     * Converts a [Filter] to its legacy representation.
     */
    public object Legacy : FilterConverter<Filter, List<String>>() {

        override fun invoke(input: Filter): List<String> {
            return toLegacy(input, escape = true)
        }

        /**
         * Same as [Legacy], but without quotes.
         */
        public object Unquoted : FilterConverter<Filter, List<String>>() {

            override fun invoke(input: Filter): List<String> {
                return toLegacy(input, escape = false)
            }
        }

        private fun toLegacy(filter: Filter, escape: Boolean): List<String> {
            return when (filter) {
                is Filter.Facet -> filter.toLegacy(escape)
                is Filter.Tag -> filter.toLegacy(escape)
                is Filter.Numeric -> filter.toLegacy(escape)
            }
        }
    }
}
